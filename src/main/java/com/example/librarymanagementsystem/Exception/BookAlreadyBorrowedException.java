package com.example.librarymanagementsystem.Exception;

public class BookAlreadyBorrowedException extends RuntimeException {
    public BookAlreadyBorrowedException(String message) {
    super(message);
    }
}
