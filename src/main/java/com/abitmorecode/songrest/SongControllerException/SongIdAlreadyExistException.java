package com.abitmorecode.songrest.SongControllerException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="song id already exists")
public class SongIdAlreadyExistException extends Exception {
	public SongIdAlreadyExistException() {
		super();
	}

	public SongIdAlreadyExistException(String message) {
		super(message);
	}
}
