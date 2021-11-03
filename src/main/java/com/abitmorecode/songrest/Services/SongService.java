package com.abitmorecode.songrest.Services;

import com.abitmorecode.songrest.Controller.SongController;
import com.abitmorecode.songrest.Models.Song;
import com.abitmorecode.songrest.SongControllerException.NoIdAvailableException;
import com.abitmorecode.songrest.SongControllerException.SameSongAlreadyExistException;
import com.abitmorecode.songrest.SongControllerException.SongDoesntExistException;
import com.abitmorecode.songrest.SongControllerException.SongIdAlreadyExistException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Song Controller (Service)
 */

@Service
public class SongService implements SongsManager {

	private static final Logger log = LoggerFactory.getLogger(SongController.class);

	private static final List<Song> songs = new ArrayList<>();

	private static final Gson gson = new Gson();

	/**
	 * default constructor
	 */
	public SongService() {
		String filepath = "exercises/1/songs.json";
		try {
			init(filepath);
		} catch (IOException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		}
	}

	/**
	 * init a json via a file (filepath)
	 * <p>
	 * on corrupted/malformed json, it will either ignore it/throw an error it if the file still
	 * <p>
	 * inherits basic structure, or throws a RuntimeException
	 *
	 * @param filepath file path to json file
	 *
	 * @throws IOException thrown, if file doesn't exist or file can't be read
	 */
	public void init(String filepath) throws IOException {
		// load in string
		Stream<String> linesStream = Files.lines(Path.of(filepath));
		AtomicReference<String> allLines = new AtomicReference<>("");
		Arrays.stream(linesStream.toArray()).forEach((line) -> allLines.updateAndGet(v -> v + line));

		// remove all the spaces
		String lines = allLines.get().replaceAll("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", "");

		// format Json into Array of Songs
		Song[] loadedIn;
		// init empty array, if no object/s was/were found / loaded in
		if ((loadedIn = gson.fromJson(lines, Song[].class)) == null) {
			log.warn("no jsons found to load in from file: " + filepath + ", no songs where found");
			loadedIn = new Song[]{};
		}

		// remove any Song with null in parameters
		List<Song> nullList = Arrays.stream(loadedIn).filter(Song::anyNull).collect(Collectors.toList());
		List<Song> clonedList = Arrays.stream(loadedIn).collect(Collectors.toList());
		nullList.forEach(s -> log.warn(s.toString() + " was not added due to at least one 'null' parameter"));
		clonedList.removeAll(nullList);

		// set the cleaned List as the officially running Song List
		songs.addAll(clonedList.stream().sorted(Comparator.comparingInt(Song::getId)).collect(Collectors.toList()));
		log.info(clonedList.size() + " Songs initialized");
	}

	@Override
	public Song getSpecificSong(int id) throws SongDoesntExistException {
		if (idAlreadyExist(id)) {
			synchronized (songs) {
				//noinspection OptionalGetWithoutIsPresent
				return songs.stream().filter(s -> s.getId() == id).findFirst().get();
			}
		}
		throw new SongDoesntExistException("Song with id: " + id + " doesn't exist");
	}

	@Override
	public List<Song> getAllSongs() {
		return songs;
	}

	/**
	 * adds a Song Object
	 *
	 * @param song Song
	 *
	 * @throws NoIdAvailableException if there is no unused positive integer id
	 */
	@Override
	public void addSong(Song song) throws NoIdAvailableException {
		if (idAlreadyExist(song.getId())) {
			int newId = getFirstUnusedId();
			song.setId(newId);
			log.warn("tried to add song with id: " + song.getId() + " wich is already in use, gonna use " + newId + " instead");
		}

		synchronized (songs) {
			songs.add(song);
		}

		log.info(song.getTitle() + " was added");
	}


	@Override
	public void deleteSong(int id) throws SongDoesntExistException {
		if (idAlreadyExist(id)) {
			//noinspection OptionalGetWithoutIsPresent
			Song song = songs.stream().filter(s -> s.getId() == id).findFirst().get();
			synchronized (songs) {
				songs.remove(song);
			}
			log.info(song.getTitle() + " was removed");
		}else {
			throw new SongDoesntExistException("Song with id: " + id + " doesn't exist");
		}
	}

	@Override
	public void reset() {
		synchronized (songs) {
			songs.removeAll(new ArrayList<>(songs));
		}
		log.info("song list got cleared");
	}

	/**
	 * gets the last id of the local Song List
	 *
	 * @return int id
	 */
	private int getLastId() {
		if (songs.isEmpty()) {
			return 0;
		}
		synchronized (songs) {
			//noinspection OptionalGetWithoutIsPresent
			return songs.stream().parallel().max((s1, s2) -> Math.max(s1.getId(), s2.getId())).get().getId();
		}
	}

	/**
	 * returns the smallest Integer which meets the following criteria:
	 * <ul>
	 * 	<li>is bigger or equal to 0</li>
	 * 	<li>there exists no song with an id equal to the returned integer</li>
	 * </ul>
	 * @return first unused song id
	 * @throws NoIdAvailableException if there is no positive Integer not already used as song id
	 */
	private int getFirstUnusedId() throws NoIdAvailableException {
		int id = -1;
		boolean isAvailable = false;

		while(!isAvailable){
			isAvailable = true;
			id++;

			for(Song song: songs){
				if(song.getId() == id){
					isAvailable = false;
					break;
				}
			}

			if(id == Integer.MAX_VALUE)throw new NoIdAvailableException("No unused song id available");
		}

		return id;
	}

	/**
	 * gives back a boolean, if id already exist in Song List
	 *
	 * @param id int id
	 *
	 * @return boolean, exists or not?
	 */
	private boolean idAlreadyExist(int id) {
		synchronized (songs) {
			return songs.stream().parallel().anyMatch(s -> s.getId() == id);
		}
	}

	/**
	 * checks if song already exists in list
	 *
	 * @param song Song to check
	 *
	 * @return boolean, already exist or not
	 */
	private boolean songAlreadyExist(Song song) {
		synchronized (songs) {
			return songs.stream().parallel().anyMatch(song::equals);
		}
	}
}
