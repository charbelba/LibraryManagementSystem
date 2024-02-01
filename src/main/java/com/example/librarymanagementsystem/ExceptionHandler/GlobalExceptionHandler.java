package com.example.librarymanagementsystem.ExceptionHandler;


import com.example.librarymanagementsystem.Exception.ApiException;
import com.example.librarymanagementsystem.Exception.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleException(ApiRequestException ex) {
        ApiException exception = new ApiException(
                ex.getMessage(),
                ex.getCause(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST);
    }
}
