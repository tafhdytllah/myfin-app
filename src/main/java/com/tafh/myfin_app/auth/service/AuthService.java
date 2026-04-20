package com.tafh.myfin_app.auth.service;

import com.tafh.myfin_app.auth.dto.internal.LoginResult;
import com.tafh.myfin_app.auth.dto.internal.RefreshResult;
import com.tafh.myfin_app.auth.dto.internal.RotateTokenResult;
import com.tafh.myfin_app.auth.dto.request.LoginRequest;
import com.tafh.myfin_app.auth.dto.request.RegisterRequest;
import com.tafh.myfin_app.auth.dto.response.LoginResponse;
import com.tafh.myfin_app.common.exception.BadRequestException;
import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.common.security.JwtService;
import com.tafh.myfin_app.common.util.LogHelper;
import com.tafh.myfin_app.refresh_token.service.RefreshTokenService;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.model.RoleEnum;
import com.tafh.myfin_app.user.mapper.UserMapper;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserProfileResponse register(RegisterRequest request) {
        LogHelper.info("AUTH_REGISTER username={} email={}", request.getUsername(), request.getEmail());

        String username = request.getUsername().trim();
        String email = request.getEmail().trim();
        String rawPassword = request.getPassword();

        validateUniqueUser(username, email);

        String hashedPassword = passwordEncoder.encode(rawPassword);

        UserEntity user = UserEntity.create(
                username,
                email,
                hashedPassword,
                RoleEnum.USER
        );

        UserEntity savedUser = userRepository.save(user);

        LogHelper.info("AUTH_REGISTER_SUCCESS userId={}", savedUser.getId());
        return userMapper.toUserProfileResponse(savedUser);
    }

    @Transactional
    public LoginResult login(LoginRequest request) {
        LogHelper.info("AUTH_LOGIN username={}", request.getUsername());

        String username = request.getUsername().trim();
        String rawPassword = request.getPassword();

        UserEntity user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!user.isActive() || !passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole().name());
        String rawRefreshToken = jwtService.generateRefreshToken(user.getId());

        refreshTokenService.saveRefreshToken(user, rawRefreshToken);

        LoginResponse loginResponse =  LoginResponse.builder()
                .accessToken(accessToken)
                .expiresIn(jwtService.getAccessTokenExpiration())
                .build();

        LogHelper.info("AUTH_LOGIN_SUCCESS userId={}", user.getId());

        return LoginResult.builder()
                .response(loginResponse)
                .refreshToken(rawRefreshToken)
                .build();
    }

    @Transactional
    public RefreshResult refreshAccessToken(String rawRefreshToken) {
        LogHelper.info("AUTH_REFRESH attempt");

        validateRefreshToken(rawRefreshToken);

        RotateTokenResult rotateTokenResult = refreshTokenService.rotateRefreshToken(rawRefreshToken);
        UserEntity user = rotateTokenResult.getUser();

        validateActiveUser(user);

        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getRole().name());

        LogHelper.info("AUTH_REFRESH_SUCCESS userId={}", user.getId());

        return RefreshResult.builder()
                .response(buildLoginResponse(newAccessToken))
                .refreshToken(rotateTokenResult.getNewRefreshToken())
                .build();
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        LogHelper.info("AUTH_LOGOUT attempt");

        validateRefreshToken(rawRefreshToken);

        refreshTokenService.revokeRefreshToken(rawRefreshToken);

        LogHelper.info("AUTH_LOGOUT_SUCCESS");
    }

    private void validateUniqueUser(String username, String email) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("username", "Username already exists");
        }
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("email", "Email already exists");
        }
    }

    private void validateRefreshToken(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("Refresh token is missing");
        }

        try {
            String type = jwtService.parseClaims(token).get("type", String.class);

            if (!"refresh".equals(type)) {
                throw new UnauthorizedException("Invalid token type");
            }

        } catch (Exception e) {
            throw new UnauthorizedException("Invalid refresh token");
        }
    }

    private void validateActiveUser(UserEntity user) {
        if (!user.isActive()) {
            throw new UnauthorizedException("User is inactive");
        }
    }

    private LoginResponse buildLoginResponse(String accessToken) {
        return LoginResponse.builder()
                .accessToken(accessToken)
                .expiresIn(jwtService.getAccessTokenExpiration())
                .build();
    }

}
