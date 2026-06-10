package com.example.demo.service;

import com.example.demo.dto.auth.*;
import com.example.demo.entity.*;
import com.example.demo.enums.RoleName;
import com.example.demo.repository.*;
import com.example.demo.security.JwtService;
import com.example.demo.service.AuthService;
import com.example.demo.service.RefreshTokenService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl
        implements AuthService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    private final TokenBlacklistRepository blacklistRepository;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            throw new RuntimeException("Email already exists");
        }

        Role role = roleRepository.findByName(
                RoleName.valueOf(request.getRole())
        ).orElseThrow();

        User user =
                User.builder()
                        .fullName(request.getFullName())
                        .email(request.getEmail())
                        .password(
                                passwordEncoder.encode(
                                        request.getPassword()
                                )
                        )
                        .enabled(true)
                        .roles(Set.of(role))
                        .build();

        userRepository.save(user);
    }

    @Override
    public JwtResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElseThrow();

        String accessToken =
                jwtService.generateAccessToken(
                        user.getEmail()
                );

        String refreshToken =
                refreshTokenService
                        .createRefreshToken(user)
                        .getToken();

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .type("Bearer")
                .build();
    }

    @Override
    public JwtResponse refreshToken(
            RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.verifyToken(
                        request.getRefreshToken()
                );

        String accessToken =
                jwtService.generateAccessToken(
                        refreshToken.getUser().getEmail()
                );

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .type("Bearer")
                .build();
    }

    @Override
    public void logout(String token) {

        TokenBlacklist blacklist =
                TokenBlacklist.builder()
                        .token(token)
                        .expiredAt(
                                LocalDateTime.now().plusDays(1)
                        )
                        .build();

        blacklistRepository.save(blacklist);
    }

    @Override
    public void forgotPassword(
            ForgotPasswordRequest request
    ) {

        User user =
                userRepository
                        .findByEmail(
                                request.getEmail()
                        )
                        .orElseThrow();

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        userRepository.save(user);
    }
}