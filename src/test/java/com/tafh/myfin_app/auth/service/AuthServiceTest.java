package com.tafh.myfin_app.auth.service;

import com.tafh.myfin_app.auth.dto.internal.LoginResult;
import com.tafh.myfin_app.auth.dto.internal.RefreshResult;
import com.tafh.myfin_app.auth.dto.internal.RotateTokenResult;
import com.tafh.myfin_app.auth.dto.request.LoginRequest;
import com.tafh.myfin_app.auth.dto.request.RegisterRequest;
import com.tafh.myfin_app.common.exception.BadRequestException;
import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.common.security.JwtService;
import com.tafh.myfin_app.refresh_token.service.RefreshTokenService;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.model.Role;
import com.tafh.myfin_app.user.mapper.UserMapper;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * UNIT TEST AUTH SERVICE
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

        @InjectMocks private AuthService authService;
        @Mock private UserRepository userRepository;
        @Mock private UserMapper userMapper;
        @Mock private RefreshTokenService refreshTokenService;
        @Mock private JwtService jwtService;
        @Mock private PasswordEncoder passwordEncoder;

        private RegisterRequest registerRequest;
        private LoginRequest loginRequest;

        @BeforeEach
        void setUp() {
            registerRequest = new RegisterRequest();
            registerRequest.setUsername("john");
            registerRequest.setEmail("john@gmail.com");
            registerRequest.setPassword("password123");

            loginRequest = new LoginRequest();
            loginRequest.setUsername("john");
            loginRequest.setPassword("password123");
        }

    @Test
    void testRegisterUser_success() {

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed");
        UserEntity mappedUser = new UserEntity("john", "john@gmail.com", "hashed", Role.USER);
        when(userMapper.toUserEntity("john", "john@gmail.com", "hashed", Role.USER)).thenReturn(mappedUser);
        when(userRepository.save(mappedUser)).thenReturn(mappedUser);
        UserProfileResponse response = UserProfileResponse.builder()
                .username("john")
                .email("john@gmail.com")
                .role(Role.USER)
                .isActive(true)
                .build();
        when(userMapper.toUserProfileResponse(mappedUser)).thenReturn(response);

        UserProfileResponse result = authService.register(registerRequest);

        assertNotNull(result);
        assertEquals("john", result.getUsername());
        verify(userRepository, times(1)).save(mappedUser);
    }

    @Test
    void testRegisterUser_usernameAlreadyExists() {
        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testRegisterUser_emailAlreadyExists() {
        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@gmail.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(registerRequest));

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void testLoginUser_success() {
        UserEntity user = new UserEntity("john", "john@gmail.com", "hashed", Role.USER);
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashed")).thenReturn(true);
        when(jwtService.generateAccessToken(user.getId(), "USER")).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user.getId())).thenReturn("refresh-token");

        LoginResult result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("access-token", result.getResponse().getAccessToken());
        assertEquals("refresh-token", result.getRefreshToken());
        verify(refreshTokenService, times(1)).saveRefreshToken(user, "refresh-token");
    }

    @Test
    void testLoginUser_loginInvalid() {
        UserEntity user = new UserEntity("john", "john@gmail.com", "hashed", Role.USER);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));

        verify(jwtService, never()).generateAccessToken(any(), any());
        verify(jwtService, never()).generateRefreshToken(any());
        verify(refreshTokenService, never()).saveRefreshToken(any(), any());
    }

    @Test
    void testRefreshToken_success() {
        String rawToken = "refresh-token";
        UserEntity user = new UserEntity("john", "john@gmail.com", "hashed", Role.USER);
        RotateTokenResult rotateResult = new RotateTokenResult(user, "new-refresh");
        when(refreshTokenService.rotateRefreshToken(rawToken)).thenReturn(rotateResult);
        when(jwtService.generateAccessToken(user.getId(), "USER")).thenReturn("new-access");

        RefreshResult result = authService.refreshAccessToken(rawToken);

        assertEquals("new-access", result.getResponse().getAccessToken());
        assertEquals("new-refresh", result.getRefreshToken());
    }

    @Test
    void testRefreshToken_tokenIsNull() {
        assertThrows(UnauthorizedException.class, () -> authService.refreshAccessToken(null));
        verify(jwtService, never()).generateAccessToken(any(), any());
    }

    @Test
    void testRefreshToken_tokenIsBlank() {
        assertThrows(UnauthorizedException.class, () -> authService.refreshAccessToken(" "));
        verify(jwtService, never()).generateAccessToken(any(), any());
    }

    @Test
    void testRefreshToken_tokenInvalid() {
        when(refreshTokenService.rotateRefreshToken("bad-token")).thenThrow(new UnauthorizedException("Invalid refresh token"));

        assertThrows(UnauthorizedException.class, () -> authService.refreshAccessToken("bad-token"));

        verify(jwtService, never()).generateAccessToken(any(), any());
    }

    @Test
    void testLogoutUser_success() {
        String token = "refresh-token";

        authService.logout(token);

        verify(refreshTokenService, times(1)).revokeRefreshToken(token);
    }

    @Test
    void logout_shouldThrow_whenTokenNull() {
        assertThrows(UnauthorizedException.class, () -> authService.logout(null));

        verify(refreshTokenService, never()).revokeRefreshToken(any());
    }

    @Test
    void logout_shouldThrow_whenTokenBlank() {
        assertThrows(UnauthorizedException.class, () -> authService.logout(" "));

        verify(refreshTokenService, never()).revokeRefreshToken(any());
    }

    @Test
    void logout_shouldThrow_whenTokenInvalid() {
        doThrow(new UnauthorizedException("Invalid refresh token")).when(refreshTokenService).revokeRefreshToken("bad-token");

        assertThrows(UnauthorizedException.class, () -> authService.logout("bad-token"));

        verify(refreshTokenService).revokeRefreshToken("bad-token");
    }
}