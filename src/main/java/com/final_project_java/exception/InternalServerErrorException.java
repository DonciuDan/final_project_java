package com.final_project_java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ValueConstants;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) //500
public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException(String message) {
        super(message);
    }
}
