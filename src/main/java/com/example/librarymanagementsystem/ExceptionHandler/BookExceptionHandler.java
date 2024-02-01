package com.example.librarymanagementsystem.ExceptionHandler;

import com.example.librarymanagementsystem.Exception.BookAlreadyBorrowedException;
import com.example.librarymanagementsystem.Exception.BookException;
import com.example.librarymanagementsystem.Exception.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BookExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex) {
        BookException exception = new BookException(ex.getMessage(),ex.getCause(),HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exception,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookAlreadyBorrowedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleBookAlreadyBorrowedException(BookAlreadyBorrowedException ex) {
        BookException exception = new BookException(ex.getMessage(),ex.getCause(),HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exception,HttpStatus.CONFLICT);
    }
}
