package com.example.librarymanagementsystem.ExceptionHandler;


import com.example.librarymanagementsystem.Exception.ApiException;
import com.example.librarymanagementsystem.Exception.ApiRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiRequestException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiException> handleException(ApiRequestException ex) {
        ApiException exception = new ApiException(
                ex.getMessage(),
                ex.getCause(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(exception,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiException> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errorList = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        System.out.println("bad request");
        ApiException exception = ApiException.builder()
                .message(errorList.toString())
                .error(ex.getCause())
                .status(HttpStatus.BAD_REQUEST)
                .zonedDateTime(ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
