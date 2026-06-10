package com.example.demo.service;

import com.example.demo.dto.auth.*;

public interface AuthService {

    void register(RegisterRequest request);

    JwtResponse login(LoginRequest request);

    JwtResponse refreshToken(RefreshTokenRequest request);

    void logout(String token);

    void forgotPassword(
            ForgotPasswordRequest request
    );
}