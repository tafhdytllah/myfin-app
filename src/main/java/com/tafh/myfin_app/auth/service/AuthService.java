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
import com.tafh.myfin_app.user.enums.Role;
import com.tafh.myfin_app.user.mapper.UserMapper;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import com.tafh.myfin_app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
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

        if (userRepository.existsByUsername(username)) throw new BadRequestException("username", "username already exists");
        if (userRepository.existsByEmail(email)) throw new BadRequestException("email", "email already exists");

        String hashedPassword = passwordEncoder.encode(rawPassword);

        UserEntity user = userMapper.toUserEntity(
                username,
                email,
                hashedPassword,
                Role.USER
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

        UserEntity user = userService.verifyUserLogin(username, rawPassword);

        String accessToken = jwtService.generateAccessToken(user.getId(), user.getRole().name());
        String rawRefreshToken = jwtService.generateRefreshToken(user.getId());

        LoginResponse loginResponse =  LoginResponse.builder()
                .accessToken(accessToken)
                .build();

        refreshTokenService.saveRefreshToken(user, rawRefreshToken);

        LogHelper.info("AUTH_LOGIN_SUCCESS userId={}", user.getId());
        return LoginResult.builder()
                .response(loginResponse)
                .refreshToken(rawRefreshToken)
                .build();
    }

    @Transactional
    public RefreshResult refreshAccessToken(String rawRefreshToken) {
        LogHelper.info("AUTH_REFRESH attempt");

        if (rawRefreshToken == null || rawRefreshToken.isBlank()) throw new UnauthorizedException("Refresh token is missing");

        // rotate token (revoke old + create new
        RotateTokenResult rotateTokenResult = refreshTokenService.rotateRefreshToken(rawRefreshToken);

        UserEntity user = rotateTokenResult.getUser();

        String newAccessToken = jwtService.generateAccessToken(user.getId(), user.getRole().name());

        LoginResponse response =  LoginResponse.builder()
                .accessToken(newAccessToken)
                .build();

        LogHelper.info("AUTH_REFRESH_SUCCESS userId={}", user.getId());
        return RefreshResult.builder()
                .response(response)
                .refreshToken(rotateTokenResult.getNewRefreshToken())
                .build();
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        LogHelper.info("AUTH_LOGOUT attempt");

        if (rawRefreshToken == null || rawRefreshToken.isBlank()) throw new UnauthorizedException("Refresh token is missing");

        refreshTokenService.revokeRefreshToken(rawRefreshToken);
        LogHelper.info("AUTH_LOGOU_SUCCESS");
    }

}
