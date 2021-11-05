package com.abitmorecode.songrest.Services;

import com.abitmorecode.songrest.Models.Song;
import com.abitmorecode.songrest.SongControllerException.NoIdAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SongServiceTest {

	@Autowired
	private SongService songService;

	private String correctfile;
	private String wrongfile;
	private String weirdfile;
	private String nonefile;

	@BeforeEach
	void setup() {
		songService.reset();
		correctfile = "correct_songs.json";
		wrongfile = "incorrect.json";
		weirdfile = "incorrect_songs.json";
		nonefile = "somepath";
	}

	@Test
	void testFilesExist() {
		assertNotNull(correctfile);
		assertNotNull(wrongfile);
		assertNotNull(weirdfile);
		assertNotNull(nonefile);
		assert(new File(Objects.requireNonNull(getClass().getClassLoader().getResource(correctfile)).getPath()).exists());
		assert(new File(Objects.requireNonNull(getClass().getClassLoader().getResource(wrongfile)).getPath()).exists());
		assert(new File(Objects.requireNonNull(getClass().getClassLoader().getResource(weirdfile)).getPath()).exists());
		assertThrows(NullPointerException.class, () -> new File(Objects.requireNonNull(getClass().getClassLoader().getResource(nonefile)).getPath()));
	}

	@Test
	void basicInitializeTest() {
		songService.init(correctfile);
		List<Song> songs = songService.getAllSongs();
		assertEquals(new Song(1, "Das Test", "N bisschen Test", "TestTestTest", 2015), songs.stream().findFirst().get());
	}

	@Test
	void pathNotFoundTest() {
		songService.init(nonefile);
		System.out.println(Arrays.toString(songService.getAllSongs().toArray()));
		assert songService.getAllSongs().isEmpty();
	}

	@Test
	void fileHasNoJsonInItTest() {
		songService.init(wrongfile);
		System.out.println(Arrays.toString(songService.getAllSongs().toArray()));
		assert songService.getAllSongs().isEmpty();
	}

	@Test
	void fileHasWrongJsonInItTest() {
		songService.init(weirdfile);
		assert songService.getAllSongs().isEmpty();
	}

	@Test
	void addASimpleSong() {
		try {
			songService.addSong(new Song(1, "Das Test", "N bisschen Test", "TestTestTest", 2015));
		} catch (NoIdAvailableException e) {
			fail();
		}
		assertEquals(new Song(1, "Das Test", "N bisschen Test", "TestTestTest", 2015), songService.getAllSongs().stream().findFirst().get());
	}
}