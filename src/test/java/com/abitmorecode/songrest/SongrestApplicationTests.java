package com.abitmorecode.songrest;

import com.abitmorecode.songrest.Controller.SongController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SongrestApplicationTests {

	private static SongController songController;

	@BeforeAll
	static void beforeAll() {
		songController = new SongController();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getSong() {

	}

	@Test
	void getSongs() {

	}
}
