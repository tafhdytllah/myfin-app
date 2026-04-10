package com.tafh.myfin_app.refresh_token.service;

import com.tafh.myfin_app.auth.dto.internal.RotateTokenResult;
import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.common.security.JwtService;
import com.tafh.myfin_app.common.util.HashHelper;
import com.tafh.myfin_app.config.properties.JwtProperties;
import com.tafh.myfin_app.refresh_token.model.RefreshTokenEntity;
import com.tafh.myfin_app.refresh_token.repository.RefreshTokenRepository;
import com.tafh.myfin_app.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final JwtService jwtService;

    private final JwtProperties jwtProperties;

    @Transactional
    public void saveRefreshToken(UserEntity user, String rawRefreshToken) {

        String hashedToken = HashHelper.sha256(rawRefreshToken);

        Instant expiryDate = Instant.now().plus(jwtProperties.getRefreshExpiration(), ChronoUnit.MILLIS);

        RefreshTokenEntity newToken = RefreshTokenEntity.createRefreshToken(user, hashedToken, expiryDate);

        refreshTokenRepository.save(newToken);
    }

    @Transactional
    public RotateTokenResult rotateRefreshToken(String rawToken) {
        String hashedToken = HashHelper.sha256(rawToken);

        RefreshTokenEntity token = refreshTokenRepository.findByTokenHashAndRevokedIsFalse(hashedToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (!token.isActive()) throw new UnauthorizedException("Refresh token is expired");

        UserEntity user = token.getUser();

        token.revoke();

        String newRawToken = jwtService.generateRefreshToken(user.getId());

        saveRefreshToken(user, newRawToken);

        return RotateTokenResult.builder()
                .user(user)
                .newRefreshToken(newRawToken)
                .build();
    }

    @Transactional
    public void revokeRefreshToken(String rawRefreshToken) {
        String hashedToken = HashHelper.sha256(rawRefreshToken);

        RefreshTokenEntity token = refreshTokenRepository.findByTokenHashAndRevokedIsFalse(hashedToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        token.revoke();
    }

}
