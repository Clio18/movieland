package com.tteam.movieland.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RandomMoviesException extends RuntimeException {
    public RandomMoviesException(String message) {
        super(message);
    }
}
