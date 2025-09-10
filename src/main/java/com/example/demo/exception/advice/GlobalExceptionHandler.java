package com.example.demo.exception.advice;

import com.example.demo.exception.InvalidActiveEmployeeException;
import com.example.demo.exception.InvalidAgeEmployeeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseException responseExceptionHandler(Exception e) {
        return new ResponseException(e.getMessage());
    }

    @ExceptionHandler({InvalidAgeEmployeeException.class, InvalidActiveEmployeeException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseException invalidExceptionHandler(Exception e) {
        return new ResponseException(e.getMessage());
    }
}
