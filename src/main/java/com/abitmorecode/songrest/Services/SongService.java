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

	private static final Logger log = LoggerFactory.getLogger(SongService.class);
	private final List<Song> songs = new ArrayList<>();

	private final SongInitService initializer;

	/**
	 * default constructor
	 */
	@Autowired
	public SongService(SongInitService initializer) {
		this.initializer = initializer;
		init();
	}


	private void init() {
		URL resource = getClass().getClassLoader().getResource("songs.json");
		try {
			songs.addAll(initializer.init(Objects.requireNonNull(resource).getPath()));
		} catch (IOException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		} catch (NullPointerException ex) {
			log.error("set path is null! couldn't finish initialisation!");
		} catch (ParseException e) {
			log.error("couldn't parse Json from given file! couldn't finish initialisation!");
		}
	}

	void init(String file) {
		URL resource = getClass().getClassLoader().getResource(file);
		try {
			songs.addAll(initializer.init(Objects.requireNonNull(resource).getPath()));
		} catch (IOException e) {
			log.error(Arrays.toString(e.getStackTrace()));
		} catch (NullPointerException ex) {
			log.error("given path is null! couldn't finish initialisation!");
		} catch (ParseException e) {
			log.error("couldn't parse Json from given file! couldn't finish initialisation!");
		}
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
