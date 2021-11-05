package com.abitmorecode.songrest.Services;

import com.abitmorecode.songrest.Controller.SongController;
import com.abitmorecode.songrest.Models.Song;
import com.fasterxml.jackson.core.JsonParseException;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SongInitService {

	private static final Logger log = LoggerFactory.getLogger(SongInitService.class);

	private static final Gson gson = new Gson();

	/**
	 * init a json via a file (filepath)
	 *
	 * on corrupted/malformed json, it will either ignore it/throw an error it if the file still
	 * inherits basic structure
	 *
	 * @param filepath file path to json file
	 *
	 * @throws IOException thrown, if file doesn't exist or file can't be read
	 * @throws ParseException thrown, if Json parsing wasn't successful
	 */
	public List<Song> init(String filepath) throws IOException, ParseException {
		// load in string
		Stream<String> linesStream = Files.lines(Path.of(filepath));
		AtomicReference<String> allLines = new AtomicReference<>("");
		Arrays.stream(linesStream.toArray()).forEach((line) -> allLines.updateAndGet(v -> v + line));

		// remove all spaces
		String lines = allLines.get().replaceAll("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)", "");

		// format Json into Array of Songs
		Song[] loadedIn;
		// init empty array, if no object/s was/were found / loaded in
		try {
			if ((loadedIn = gson.fromJson(lines, Song[].class)) == null) {
				log.warn("no jsons found to load in from file: " + filepath + ", no songs where found");
				loadedIn = new Song[]{};
			}
		} catch (JsonSyntaxException e) {
			String message = "error while parsing Json";
			log.error(message);
			throw new ParseException(message, 0);
		}

		// remove any Song with null in parameters
		List<Song> nullList = Arrays.stream(loadedIn).filter(Song::anyNull).collect(Collectors.toList());
		List<Song> clonedList = Arrays.stream(loadedIn).collect(Collectors.toList());
		nullList.forEach(s -> log.warn(s.toString() + " was not added due to at least one 'null' parameter"));
		clonedList.removeAll(nullList);

		log.info(clonedList.size() + " Songs initialized");
		return clonedList.stream().sorted(Comparator.comparingInt(Song::getId)).collect(Collectors.toList());
	}
}
