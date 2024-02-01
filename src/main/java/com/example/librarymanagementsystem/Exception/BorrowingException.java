package com.example.librarymanagementsystem.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
@Data
@AllArgsConstructor
public class BorrowingException {
    private String message;
    private Throwable error;
    private HttpStatus status;


    public BorrowingException(String message,HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
