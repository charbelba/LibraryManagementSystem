package com.example.librarymanagementsystem.ExceptionHandler;

import com.example.librarymanagementsystem.Exception.PatronException;
import com.example.librarymanagementsystem.Exception.PatronNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class PatronExceptionHandler {

    @ExceptionHandler(PatronNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handlePatronNotFoundException(PatronNotFoundException ex) {
        PatronException exception = new PatronException(ex.getMessage(),HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }
}
