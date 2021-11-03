package com.abitmorecode.songrest.SongControllerException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.CONFLICT, reason="no unused id available")
public class NoIdAvailableException extends Exception{
    public NoIdAvailableException(){
        super();
    }
    public NoIdAvailableException(String message){
        super(message);
    }
}
