package com.electronicstore.service;

import com.electronicstore.model.RefreshToken;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(String email);
    RefreshToken verifyRefreshToken(String refreshToken);
}
