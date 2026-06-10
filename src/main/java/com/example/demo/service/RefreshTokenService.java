package com.example.demo.service;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(User user);

    RefreshToken verifyToken(String token);
}