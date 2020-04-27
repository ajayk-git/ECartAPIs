package com.springbootcamp.springsecurity.Exceptions;

import org.apache.tomcat.websocket.server.WsHttpUpgradeHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MetaDatafieldAlreadyExistException extends RuntimeException {

    public MetaDatafieldAlreadyExistException(String message){
        super(message);
    }
}
