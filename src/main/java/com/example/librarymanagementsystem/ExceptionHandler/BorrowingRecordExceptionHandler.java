package com.example.librarymanagementsystem.ExceptionHandler;


import com.example.librarymanagementsystem.Exception.BookException;
import com.example.librarymanagementsystem.Exception.BorrowingException;
import com.example.librarymanagementsystem.Exception.BorrowingRecordNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class BorrowingRecordExceptionHandler {


    @ExceptionHandler(BorrowingRecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleBorrowingRecordNotFoundException(BorrowingRecordNotFoundException ex) {
        BorrowingException exception = new BorrowingException(ex.getMessage(),HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(exception, HttpStatus.NOT_FOUND);
    }
}
