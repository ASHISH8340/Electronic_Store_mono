package com.electronicstore.service.serviceimpl;

import com.electronicstore.model.RefreshToken;
import com.electronicstore.model.User;
import com.electronicstore.repository.RefreshTokenRepository;
import com.electronicstore.repository.UserRepository;
import com.electronicstore.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    public long refreshTokenValidity = 12*60*60*1000;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email).get();
        RefreshToken refreshToken1 = user.getRefreshToken();

        if(refreshToken1 == null){
            refreshToken1=RefreshToken.builder().refreshToken(UUID.randomUUID().toString())
                    .expiry(Instant.now().plusMillis(refreshTokenValidity))
                    .user(userRepository.findByEmail(email).get())
                    .build();
        }else{
            refreshToken1.setExpiry(Instant.now().plusMillis(refreshTokenValidity));
        }

        user.setRefreshToken(refreshToken1);
        refreshTokenRepository.save(refreshToken1);
        return refreshToken1;
    }

    @Override
    public RefreshToken verifyRefreshToken(String refreshToken)  {

        RefreshToken refreshTokenOb = refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Given Token doesn't exist in db"));
        if(refreshTokenOb.getExpiry().compareTo(Instant.now())<0){
            refreshTokenRepository.delete(refreshTokenOb);
            throw new RuntimeException("Refresh Token Expired !!");
        }
        return refreshTokenOb;
    }
}
