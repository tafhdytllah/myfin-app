//package com.tafh.myfin_app.refresh_token.service;
//
//import com.tafh.myfin_app.auth.dto.internal.RotateTokenResult;
//import com.tafh.myfin_app.common.exception.UnauthorizedException;
//import com.tafh.myfin_app.common.security.JwtService;
//import com.tafh.myfin_app.config.properties.JwtProperties;
//import com.tafh.myfin_app.refresh_token.model.RefreshTokenEntity;
//import com.tafh.myfin_app.refresh_token.repository.RefreshTokenRepository;
//import com.tafh.myfin_app.user.model.UserEntity;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
///**
// * UNIT TEST REFRESH TOKEN SERVICE
// */
//@ExtendWith(MockitoExtension.class)
//class RefreshTokenServiceTest {
//
//    @InjectMocks private RefreshTokenService refreshTokenService;
//    @Mock private RefreshTokenRepository refreshTokenRepository;
//    @Mock private JwtService jwtService;
//    @Mock private JwtProperties jwtProperties;
//
//    private UserEntity user;
//    private String rawToken;
//
//    @BeforeEach
//    void setUp() {
//        user = new UserEntity();
//        rawToken = "raw-refresh-token";
//    }
//
//    @Test
//    void saveRefreshToken_shouldSaveToken() {
//
//        when(jwtProperties.getRefreshExpiration()).thenReturn(1000L);
//
//        refreshTokenService.saveRefreshToken(user, rawToken);
//
//        verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
//    }
//
//    @Test
//    void rotateRefreshToken_shouldReturnNewToken() {
//
//        RefreshTokenEntity token = mock(RefreshTokenEntity.class);
//
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(any()))
//                .thenReturn(Optional.of(token));
//
//        when(token.getUser()).thenReturn(user);
//        when(token.isActive()).thenReturn(true);
//
//        when(jwtService.generateRefreshToken(user.getId()))
//                .thenReturn("new-refresh-token");
//
//        RotateTokenResult result =
//                refreshTokenService.rotateRefreshToken(rawToken);
//
//        assertNotNull(result);
//        assertEquals(user, result.getUser());
//        assertEquals("new-refresh-token", result.getNewRefreshToken());
//
//        verify(token).revoke();
//        verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
//    }
//
//    @Test
//    void rotateRefreshToken_shouldThrow_whenTokenNotFound() {
//
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(any()))
//                .thenReturn(Optional.empty());
//
//        assertThrows(UnauthorizedException.class,
//                () -> refreshTokenService.rotateRefreshToken(rawToken));
//    }
//
//    @Test
//    void rotateRefreshToken_shouldThrow_whenTokenExpired() {
//
//        RefreshTokenEntity token = mock(RefreshTokenEntity.class);
//
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(any()))
//                .thenReturn(Optional.of(token));
//
//        when(token.isActive()).thenReturn(false);
//
//        assertThrows(UnauthorizedException.class,
//                () -> refreshTokenService.rotateRefreshToken(rawToken));
//    }
//
//    @Test
//    void revokeRefreshToken_shouldRevokeToken() {
//
//        RefreshTokenEntity token = mock(RefreshTokenEntity.class);
//
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(any()))
//                .thenReturn(Optional.of(token));
//
//        refreshTokenService.revokeRefreshToken(rawToken);
//
//        verify(token).revoke();
//    }
//
//    @Test
//    void revokeRefreshToken_shouldThrow_whenTokenInvalid() {
//
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(any()))
//                .thenReturn(Optional.empty());
//
//        assertThrows(UnauthorizedException.class,
//                () -> refreshTokenService.revokeRefreshToken(rawToken));
//    }
//}