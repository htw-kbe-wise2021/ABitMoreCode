package com.abitmorecode.songrest.Services;

import com.abitmorecode.songrest.Models.Song;
import com.abitmorecode.songrest.SongControllerException.NoIdAvailableException;
import com.abitmorecode.songrest.SongControllerException.SameSongAlreadyExistException;
import com.abitmorecode.songrest.SongControllerException.SongIdAlreadyExistException;
import com.google.gson.JsonParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SongServiceTest {

	@SuppressWarnings("FieldCanBeLocal")
	private final String path = "src/test/java/com/abitmorecode/songrest/test_json/";

	private String correctpath;
	private String wrongpath;
	private String weirdpath;
	private String nonepath;

	@BeforeEach
	void setup() {
		correctpath = path + "correct_songs.json";
		wrongpath = path + "incorrect.json";
		weirdpath = path + "incorrect_songs.json";
		nonepath = path + "somepath";
	}

	@Test
	void testFilesExist() {
		assertNotNull(correctpath);
		assertNotNull(wrongpath);
		assertNotNull(weirdpath);
		assertNotNull(nonepath);
		assert(new File(correctpath).exists());
		assert(new File(wrongpath).exists());
		assert(new File(weirdpath).exists());
		assert(!new File(nonepath).exists());
	}

	@Test
	void basicInitializeTest() {
		SongService songService = null;
		try {
			songService = new SongService();
			songService.reset();
			songService.init(correctpath);
		} catch (IOException e) {
			fail();
		}

		List<Song> songs = songService.getAllSongs();

		assertEquals(new Song(1, "Das Test", "N bisschen Test", "TestTestTest", 2015), songs.stream().findFirst().get());
	}

	@Test
	void pathNotFoundTest() {
		final SongService songService = new SongService();
		assertThrows(NoSuchFileException.class, () -> songService.init(nonepath));
	}

	@Test
	void fileHasNoJsonInItTest() {
		final SongService songService = new SongService();
		assertThrows(JsonParseException.class, () -> songService.init(wrongpath));
	}

	@Test
	void fileHasWrongJsonInItTest() {
		SongService songService = new SongService();
		songService.reset();
		try {
			songService.init(weirdpath);
		} catch (IOException e) {
			fail();
		}
		assert songService.getAllSongs().isEmpty();
	}

	@Test
	void addASimpleSong() {
		SongsManager songService = new SongService();
		songService.reset();
		try {
			songService.addSong(new Song(1, "Das Test", "N bisschen Test", "TestTestTest", 2015));
		} catch (NoIdAvailableException e) {
			fail();
		}
		assertEquals(new Song(1, "Das Test", "N bisschen Test", "TestTestTest", 2015), songService.getAllSongs().stream().findFirst().get());
	}

	@Test
	void addASongWithAutomaticIdAssigment() {
		SongsManager songService = new SongService();
		songService.reset();
	}

	@Test
	void addIdWhereNoIdWasBeforeTest() {
		List<String> list = new ArrayList<>();
		assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "string"));
	}
}