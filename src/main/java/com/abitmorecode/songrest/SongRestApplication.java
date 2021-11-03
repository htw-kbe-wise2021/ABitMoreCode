package com.abitmorecode.songrest;

import com.abitmorecode.songrest.Controller.SongController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SongRestApplication {
	private static final Logger log = LoggerFactory.getLogger(SongController.class);

	public static void main(String[] args) {
		log.info("server start");
		SpringApplication.run(SongRestApplication.class, args);
	}
}
