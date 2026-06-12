package com.example.demo.exception;

import com.example.demo.dto.response.MessageResponse;

import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            ResourceNotFoundException.class
    )
    public ResponseEntity<MessageResponse>
    handleNotFound(
            ResourceNotFoundException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        MessageResponse.builder()
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(
            BadRequestException.class
    )
    public ResponseEntity<MessageResponse>
    handleBadRequest(
            BadRequestException ex
    ) {

        return ResponseEntity
                .badRequest()
                .body(
                        MessageResponse.builder()
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String, String>>
    handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, String> errors =
                new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler(
            AccessDeniedException.class
    )
    public ResponseEntity<MessageResponse>
    handleAccessDenied(
            AccessDeniedException ex
    ) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        MessageResponse.builder()
                                .message("Access denied")
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse>
    handleException(
            Exception ex
    ) {

        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(
                        MessageResponse.builder()
                                .message(
                                        "Internal server error"
                                )
                                .build()
                );
    }
}