package com.example.demo.exception;

import com.example.demo.dto.response.MessageResponse;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponse> handleRuntime(
            RuntimeException ex) {

        return ResponseEntity
                .badRequest()
                .body(
                        MessageResponse.builder()
                                .message(ex.getMessage())
                                .build()
                );
    }

}