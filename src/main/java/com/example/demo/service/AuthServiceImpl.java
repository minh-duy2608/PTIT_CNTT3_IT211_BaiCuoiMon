package com.example.demo.service;

import com.example.demo.dto.auth.*;
import com.example.demo.entity.*;
import com.example.demo.enums.RoleName;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.*;
import com.example.demo.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

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
            throw new BadRequestException(
                    "Email already exists"
            );
        }

        Role role = roleRepository.findByName(
                RoleName.valueOf(request.getRole())
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        "Role not found"
                )
        );

        User user = User.builder()
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

        log.info(
                "Register user: {}",
                request.getEmail()
        );

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

        User user = userRepository.findByEmail(
                request.getEmail()
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        "User not found"
                )
        );

        log.info(
                "Login success: {}",
                request.getEmail()
        );

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
            RefreshTokenRequest request
    ) {

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
                                LocalDateTime.now()
                                        .plusDays(1)
                        )
                        .build();

        blacklistRepository.save(blacklist);

        log.info("User logout");
    }

    @Override
    public void forgotPassword(
            ForgotPasswordRequest request
    ) {

        User user =
                userRepository.findByEmail(
                        request.getEmail()
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User not found"
                        )
                );

        user.setPassword(
                passwordEncoder.encode(
                        request.getNewPassword()
                )
        );

        log.info(
                "Password changed for: {}",
                request.getEmail()
        );

        userRepository.save(user);
    }
}