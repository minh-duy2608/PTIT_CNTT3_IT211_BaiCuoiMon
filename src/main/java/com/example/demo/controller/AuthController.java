package com.example.demo.controller;

import com.example.demo.dto.auth.*;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public MessageResponse register(
            @Valid
            @RequestBody RegisterRequest request) {

        authService.register(request);

        return MessageResponse.builder()
                .message("Register success")
                .build();
    }

    @PostMapping("/login")
    public JwtResponse login(
            @Valid
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }

    @PostMapping("/refresh")
    public JwtResponse refreshToken(
            @Valid
            @RequestBody RefreshTokenRequest request) {

        return authService.refreshToken(request);
    }

    @PostMapping("/logout")
    public MessageResponse logout(
            @RequestHeader("Authorization")
            String authHeader) {

        String token =
                authHeader.substring(7);

        authService.logout(token);

        return MessageResponse.builder()
                .message("Logout success")
                .build();
    }

    @PostMapping("/forgot-password")
    public MessageResponse forgotPassword(
            @Valid
            @RequestBody ForgotPasswordRequest request
    ) {

        authService.forgotPassword(request);

        return MessageResponse.builder()
                .message("Password updated")
                .build();
    }
}