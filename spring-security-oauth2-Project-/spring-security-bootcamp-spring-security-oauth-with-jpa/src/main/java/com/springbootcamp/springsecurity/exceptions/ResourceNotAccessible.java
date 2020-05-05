package com.springbootcamp.springsecurity.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class ResourceNotAccessible extends RuntimeException {
    public ResourceNotAccessible(String message){
        super(message);
    }
}
