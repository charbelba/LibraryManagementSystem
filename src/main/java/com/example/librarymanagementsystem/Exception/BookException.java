package com.example.librarymanagementsystem.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;


@Data
@AllArgsConstructor
public class BookException  {
    private String message;
    private Throwable error;
    private HttpStatus status;


    public BookException(String message,HttpStatus status){
        this.message = message;
        this.status = status;
    }


}
