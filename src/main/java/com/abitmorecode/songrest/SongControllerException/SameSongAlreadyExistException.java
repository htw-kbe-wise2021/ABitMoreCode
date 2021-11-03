package com.abitmorecode.songrest.SongControllerException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="song already exists")
public class SameSongAlreadyExistException extends Exception {
	public SameSongAlreadyExistException() {
		super();
	}

	public SameSongAlreadyExistException(String message) {
		super(message);
	}
}
