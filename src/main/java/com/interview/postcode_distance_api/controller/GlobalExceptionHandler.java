package com.interview.postcode_distance_api.controller;

import com.interview.postcode_distance_api.exception.PostcodeAlreadyExistsException;
import com.interview.postcode_distance_api.exception.PostcodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostcodeAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handlePostcodeAlreadyExists(PostcodeAlreadyExistsException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(PostcodeNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePostcodeNotFound(PostcodeNotFoundException exception) {
        ApiErrorResponse response = new ApiErrorResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    public record ApiErrorResponse(
            Instant timestamp,
            int status,
            String error,
            String message
    ) {
    }
}

