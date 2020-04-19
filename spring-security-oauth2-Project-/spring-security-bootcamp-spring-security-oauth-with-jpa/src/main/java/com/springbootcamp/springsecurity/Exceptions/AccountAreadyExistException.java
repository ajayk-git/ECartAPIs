package com.springbootcamp.springsecurity.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class AccountAreadyExistException extends RuntimeException {
    public AccountAreadyExistException(String message) {
        super(message);
    }


}
