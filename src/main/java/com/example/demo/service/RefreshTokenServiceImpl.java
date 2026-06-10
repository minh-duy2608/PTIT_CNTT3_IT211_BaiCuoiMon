package com.example.demo.service;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl
        implements RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Override
    public RefreshToken createRefreshToken(User user) {

        repository.findByUser(user)
                .ifPresent(repository::delete);

        RefreshToken refreshToken =
                RefreshToken.builder()
                        .token(UUID.randomUUID().toString())
                        .expiryDate(
                                LocalDateTime.now().plusDays(7)
                        )
                        .user(user)
                        .build();

        return repository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyToken(String token) {

        RefreshToken refreshToken =
                repository.findByToken(token)
                        .orElseThrow(
                                () -> new RuntimeException("Refresh token not found")
                        );

        if (refreshToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            repository.delete(refreshToken);

            throw new RuntimeException("Refresh token expired");
        }

        return refreshToken;
    }
}