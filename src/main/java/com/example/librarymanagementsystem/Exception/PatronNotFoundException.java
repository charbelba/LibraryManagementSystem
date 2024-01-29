package com.example.librarymanagementsystem.Exception;

public class PatronNotFoundException extends RuntimeException {
    public PatronNotFoundException(String message) {
        super(message);
    }
}