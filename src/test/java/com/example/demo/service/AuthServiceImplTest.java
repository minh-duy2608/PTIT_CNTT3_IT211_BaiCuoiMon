package com.example.demo.service;

import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.RefreshTokenRequest;
import com.example.demo.dto.auth.RegisterRequest;
import com.example.demo.entity.*;
import com.example.demo.enums.RoleName;
import com.example.demo.repository.*;
import com.example.demo.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private TokenBlacklistRepository blacklistRepository;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerSuccess() {

        RegisterRequest request = new RegisterRequest();
        request.setFullName("Test User");
        request.setEmail("test@gmail.com");
        request.setPassword("123456");
        request.setRole("ROLE_CANDIDATE");

        Role role = Role.builder()
                .id(1L)
                .name(RoleName.ROLE_CANDIDATE)
                .build();

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(false);

        when(roleRepository.findByName(RoleName.ROLE_CANDIDATE))
                .thenReturn(Optional.of(role));

        when(passwordEncoder.encode(any()))
                .thenReturn("encoded");

        authService.register(request);

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void registerDuplicateEmail() {

        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@gmail.com");

        when(userRepository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(
                RuntimeException.class,
                () -> authService.register(request)
        );
    }

    @Test
    void loginSuccess() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@gmail.com");
        request.setPassword("123456");

        User user = User.builder()
                .id(1L)
                .email("test@gmail.com")
                .fullName("Test")
                .enabled(true)
                .roles(Set.of())
                .build();

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));

        when(jwtService.generateAccessToken(any()))
                .thenReturn("access-token");

        RefreshToken refreshToken = RefreshToken.builder()
                .token("refresh-token")
                .expiryDate(LocalDateTime.now().plusDays(7))
                .user(user)
                .build();

        when(refreshTokenService.createRefreshToken(user))
                .thenReturn(refreshToken);

        var response = authService.login(request);

        assertNotNull(response);
        assertEquals("access-token", response.getAccessToken());
        assertEquals("refresh-token", response.getRefreshToken());
    }

    @Test
    void refreshTokenSuccess() {

        User user = User.builder()
                .email("test@gmail.com")
                .build();

        RefreshToken token = RefreshToken.builder()
                .token("refresh-token")
                .user(user)
                .build();

        RefreshTokenRequest request =
                new RefreshTokenRequest();

        request.setRefreshToken("refresh-token");

        when(refreshTokenService.verifyToken("refresh-token"))
                .thenReturn(token);

        when(jwtService.generateAccessToken(any()))
                .thenReturn("new-access");

        var response =
                authService.refreshToken(request);

        assertEquals(
                "new-access",
                response.getAccessToken()
        );
    }

    @Test
    void logoutSuccess() {

        authService.logout("jwt-token");

        verify(blacklistRepository, times(1))
                .save(any(TokenBlacklist.class));
    }
}