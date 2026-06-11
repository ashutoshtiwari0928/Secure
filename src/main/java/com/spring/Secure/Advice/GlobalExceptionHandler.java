package com.spring.Secure.Advice;

import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<@NonNull String> handleUsernameNotFoundException(Exception e) {
        System.out.println("Global Exception Handler called");
        return ResponseEntity
                .badRequest()
                .body(e.getLocalizedMessage());
    }
}
