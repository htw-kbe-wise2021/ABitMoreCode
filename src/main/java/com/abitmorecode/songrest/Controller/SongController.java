package com.abitmorecode.songrest.Controller;

import com.abitmorecode.songrest.Models.Song;
import com.abitmorecode.songrest.Services.SongsManager;
import com.abitmorecode.songrest.SongControllerException.SameSongAlreadyExistException;
import com.abitmorecode.songrest.SongControllerException.SongDoesntExistException;
import com.abitmorecode.songrest.SongControllerException.SongIdAlreadyExistException;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SongController {
	private static final Logger log = LoggerFactory.getLogger(SongController.class);
	private static final Gson gson = new Gson();
	@Autowired
	private SongsManager songService;

	@GetMapping("/ABitMoreCode/songs/{id}")
	public ResponseEntity<Song> getSong(@PathVariable int id) throws SongDoesntExistException {
		return new ResponseEntity<>(songService.getSpecificSong(id), HttpStatus.OK);
	}

	@GetMapping("/ABitMoreCode/songs")
	public ResponseEntity<List<Song>> getSongs() {
		return new ResponseEntity<>(songService.getAllSongs(), HttpStatus.OK);
	}

	@PostMapping("/ABitMoreCode/songs")
	public Song postSong(@RequestBody Song song) {
		try {
			songService.addSong(song);
			return song;
		} catch (SameSongAlreadyExistException | SongIdAlreadyExistException e) {
			e.printStackTrace();
			return null;
		}
	}

	@DeleteMapping("/ABitMoreCode/songs/{id}")
	public ResponseEntity<Object> deleteSong(@PathVariable int id) throws SongDoesntExistException {
		songService.deleteSong(id);
		return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
	}
}