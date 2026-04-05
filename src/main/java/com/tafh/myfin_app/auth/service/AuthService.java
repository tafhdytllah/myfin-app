package com.tafh.myfin_app.auth.service;

import com.tafh.myfin_app.auth.dto.*;
import com.tafh.myfin_app.auth.model.RefreshTokenEntity;
import com.tafh.myfin_app.auth.repository.RefreshTokenRepository;
import com.tafh.myfin_app.common.exception.BadRequestException;
import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.common.security.JwtService;
import com.tafh.myfin_app.common.util.HashHelper;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.enums.Role;
import com.tafh.myfin_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final RefreshTokenRepository refreshTokenRepository;

    public UserProfileResponse register(RegisterRequest request) {
        String email = request.getEmail();
        String username = request.getUsername();
        String rawPassword = request.getPassword();

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already exists");
        }

        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(rawPassword);

        UserEntity user = new UserEntity(
                username,
                email,
                hashedPassword,
                Role.USER
        );

        user = userRepository.save(user);

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {

        String username = request.getUsername();
        String rawPassword = request.getPassword();

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("invalid username or password"));

        if (!user.getIsActive()) {
            throw new UnauthorizedException("invalid username or password");
        }

        boolean isPasswordMatch = passwordEncoder.matches(rawPassword, user.getPasswordHash());

        if (!isPasswordMatch) {
            throw new UnauthorizedException("invalid username or password");
        }

        String userId = user.getId();
        String userRole = user.getRole().toString();

        String accessToken = jwtService.generateAccessToken(userId, userRole);
        String refreshToken = jwtService.generateRefreshToken(userId);

        String hashedToken = HashHelper.sha256(refreshToken);
        Instant expiryDate = Instant.now().plus(30, ChronoUnit.DAYS);

        RefreshTokenEntity newToken = RefreshTokenEntity.createRefreshToken(
                user,
                hashedToken,
                expiryDate
        );

        refreshTokenRepository.revokeAllByUser(user);
        refreshTokenRepository.save(newToken);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }


    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request) {
        String rawToken = request.getRefreshToken();
        String hashedToken = HashHelper.sha256(rawToken);

        RefreshTokenEntity token = refreshTokenRepository.findByTokenHashAndRevokedIsFalse(hashedToken)
                .orElseThrow(() -> new UnauthorizedException("invalid refresh token"));

        if (!token.isActive()) {
            throw new UnauthorizedException("refresh token is expired");
        }

        UserEntity user = token.getUser();

        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getRole().toString());

        token.revoke();

        String newRefreshToken = jwtService.generateRefreshToken(user.getId());

        String newHashedToken = HashHelper.sha256(newRefreshToken);
        Instant expiryDate = Instant.now().plus(30, ChronoUnit.DAYS);

        RefreshTokenEntity newToken = RefreshTokenEntity.createRefreshToken(
                user,
                newHashedToken,
                expiryDate
        );

        refreshTokenRepository.save(newToken);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .build();

    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        String rawToken = request.getRefreshToken();
        String hashedToken = HashHelper.sha256(rawToken);

        RefreshTokenEntity token = refreshTokenRepository.findByTokenHashAndRevokedIsFalse(hashedToken)
                .orElseThrow(() -> new UnauthorizedException("invalid refresh token"));

        token.revoke();
    }

}
