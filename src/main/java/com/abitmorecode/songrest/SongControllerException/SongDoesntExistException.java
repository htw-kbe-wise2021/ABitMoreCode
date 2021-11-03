package com.abitmorecode.songrest.SongControllerException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="no such song")
public class SongDoesntExistException extends Exception {
	public SongDoesntExistException() {
		super();
	}

	public SongDoesntExistException(String message) {
		super(message);
	}
}
