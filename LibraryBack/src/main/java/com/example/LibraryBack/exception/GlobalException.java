package com.example.LibraryBack.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(NotException.class)
    public ResponseEntity<?> handleNotFound(NotException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<?> handleEmailAlreadyExists(EmailAlreadyExists e){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(e.getMessage());
    }
    @ExceptionHandler(EmailAndPasswordAreNotMatch.class)
    public ResponseEntity<?> handleEmailAndPasswordAreNotMatch(EmailAndPasswordAreNotMatch e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

}
