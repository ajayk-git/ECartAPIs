package com.springbootcamp.springsecurity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MetaDatafieldAlreadyExistException extends RuntimeException {

    public MetaDatafieldAlreadyExistException(String message){
        super(message);
    }
}
