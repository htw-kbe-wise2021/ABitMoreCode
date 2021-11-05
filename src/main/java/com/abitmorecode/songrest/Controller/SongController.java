package com.abitmorecode.songrest.Controller;

import com.abitmorecode.songrest.Models.Song;
import com.abitmorecode.songrest.Services.SongsManager;
import com.abitmorecode.songrest.SongControllerException.NoIdAvailableException;
import com.abitmorecode.songrest.SongControllerException.SongDoesntExistException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/ABitMoreCode")
public class SongController {
	private static final Logger log = LoggerFactory.getLogger(SongController.class);
	private static final XmlMapper xml = new XmlMapper();

	@Autowired
	private SongsManager songService;

	@GetMapping("/songs/{id}")
	public ResponseEntity<Object> getSong(@PathVariable int id, @RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) throws SongDoesntExistException {
		if(!isAcceptHeaderValid(acceptHeader)){
			String logInfo = "unknown accept header (=\""+acceptHeader+"\") on GET song by id request";
			log.info(logInfo);
			return new ResponseEntity<>(logInfo,HttpStatus.BAD_REQUEST);
		}

		Object song = songService.getSpecificSong(id);

		if(acceptHeader == "application/xml"){
			try {
				song = xml.writeValueAsBytes(song);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage());
			}
		}

		return new ResponseEntity<>(song, HttpStatus.OK);
	}

	@GetMapping("/songs")
	public ResponseEntity<Object> getSongs(@RequestHeader(HttpHeaders.ACCEPT) String acceptHeader) {
		if(!isAcceptHeaderValid(acceptHeader)){
			String logInfo = "unknown accept header (=\""+acceptHeader+"\") on GET songs request";
			log.info(logInfo);
			return new ResponseEntity<>(logInfo,HttpStatus.BAD_REQUEST);
		}

		Object songs = songService.getAllSongs();

		if(acceptHeader == "application/xml"){
			try {
				songs = xml.writeValueAsBytes(songs);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage());
			}
		}

		return new ResponseEntity<>(songs, HttpStatus.OK);
	}

	@PostMapping("/songs")
	public ResponseEntity<Object> postSong(@RequestBody Song song) throws NoIdAvailableException{
		songService.addSong(song);
		String location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(song.getId())
				.toUriString();
		return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).build();
	}

	@DeleteMapping("/songs/{id}")
	public ResponseEntity<Object> deleteSong(@PathVariable int id) {
		try {
			songService.deleteSong(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (SongDoesntExistException e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	private boolean isAcceptHeaderValid(String acceptHeader){
		return acceptHeader == "application/json" || acceptHeader == "application/xml";
	}
}
