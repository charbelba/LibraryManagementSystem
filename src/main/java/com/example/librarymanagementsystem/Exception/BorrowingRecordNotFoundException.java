package com.example.librarymanagementsystem.Exception;

public class BorrowingRecordNotFoundException extends RuntimeException {
    public BorrowingRecordNotFoundException(String message) {
        super(message);
    }
}