package com.example.librarymanagementsystem.Exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;


@Data
@AllArgsConstructor
public class ApiException  {
    private String message;
    private Throwable error;
    private HttpStatus status;
    private ZonedDateTime zonedDateTime;

    public ApiException(String message,HttpStatus status){
        this.message = message;
        this.status = status;
    }
}
