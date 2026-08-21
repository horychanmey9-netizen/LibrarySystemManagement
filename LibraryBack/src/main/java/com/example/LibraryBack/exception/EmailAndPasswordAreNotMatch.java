package com.example.LibraryBack.exception;

public class EmailAndPasswordAreNotMatch extends RuntimeException {
    public EmailAndPasswordAreNotMatch(String message) {
        super(message);
    }
}
