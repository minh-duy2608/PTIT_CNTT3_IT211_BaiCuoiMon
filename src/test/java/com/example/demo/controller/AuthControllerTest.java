package com.example.demo.controller;

import com.example.demo.dto.auth.*;
import com.example.demo.dto.response.MessageResponse;
import com.example.demo.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void loginSuccess() {

        JwtResponse response =
                JwtResponse.builder()
                        .accessToken("token")
                        .refreshToken("refresh")
                        .type("Bearer")
                        .build();

        when(authService.login(any()))
                .thenReturn(response);

        LoginRequest request = new LoginRequest();
        request.setEmail("admin@gmail.com");
        request.setPassword("123456");

        JwtResponse result =
                authController.login(request);

        assertNotNull(result);
        assertEquals(
                "token",
                result.getAccessToken()
        );
    }

    @Test
    void registerSuccess() {

        doNothing()
                .when(authService)
                .register(any());

        RegisterRequest request =
                new RegisterRequest();

        request.setFullName("Test");
        request.setEmail("test@gmail.com");
        request.setPassword("123456");
        request.setRole("ROLE_CANDIDATE");

        MessageResponse response =
                authController.register(request);

        assertEquals(
                "Register success",
                response.getMessage()
        );
    }

    @Test
    void refreshTokenSuccess() {

        JwtResponse response =
                JwtResponse.builder()
                        .accessToken("new-token")
                        .refreshToken("refresh")
                        .type("Bearer")
                        .build();

        when(authService.refreshToken(any()))
                .thenReturn(response);

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("abc123");

        JwtResponse result =
                authController.refreshToken(request);

        assertEquals(
                "new-token",
                result.getAccessToken()
        );
    }

    @Test
    void logoutSuccess() {

        doNothing()
                .when(authService)
                .logout(any());

        MessageResponse response =
                authController.logout(
                        "Bearer abc123"
                );

        assertEquals(
                "Logout success",
                response.getMessage()
        );
    }

    @Test
    void loginReturnBearerType() {

        JwtResponse response =
                JwtResponse.builder()
                        .accessToken("token")
                        .refreshToken("refresh")
                        .type("Bearer")
                        .build();

        when(authService.login(any()))
                .thenReturn(response);

        LoginRequest request =
                new LoginRequest();

        request.setEmail("a@gmail.com");
        request.setPassword("123456");

        JwtResponse result =
                authController.login(request);

        assertEquals(
                "Bearer",
                result.getType()
        );
    }
}