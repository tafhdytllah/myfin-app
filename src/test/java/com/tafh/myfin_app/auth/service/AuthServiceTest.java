//package com.tafh.myfin_app.auth.service;
//
//import com.tafh.myfin_app.auth.dto.request.LoginRequest;
//import com.tafh.myfin_app.auth.dto.response.LoginResponse;
//import com.tafh.myfin_app.auth.dto.request.RefreshTokenRequest;
//import com.tafh.myfin_app.auth.dto.request.RegisterRequest;
//import com.tafh.myfin_app.refresh_token.model.RefreshTokenEntity;
//import com.tafh.myfin_app.refresh_token.repository.RefreshTokenRepository;
//import com.tafh.myfin_app.common.exception.BadRequestException;
//import com.tafh.myfin_app.common.exception.UnauthorizedException;
//import com.tafh.myfin_app.common.security.JwtService;
//import com.tafh.myfin_app.common.util.HashHelper;
//import com.tafh.myfin_app.user.dto.UserProfileResponse;
//import com.tafh.myfin_app.user.enums.Role;
//import com.tafh.myfin_app.user.model.UserEntity;
//import com.tafh.myfin_app.user.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * UNIT TEST AUTH SERVICE
// */
//@ExtendWith(MockitoExtension.class)
//class AuthServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private RefreshTokenRepository refreshTokenRepository;
//
//    @Mock
//    private JwtService jwtService;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private AuthService authService;
//
//    private RegisterRequest registerRequest;
//    private LoginRequest loginRequest;
//
//    @BeforeEach
//    void setUp() {
//        // Initialize test input data (simulate client request payload)
//        registerRequest = new RegisterRequest();
//        registerRequest.setUsername("john");
//        registerRequest.setEmail("john@gmail.com");
//        registerRequest.setPassword("password123");
//
//        loginRequest = new LoginRequest();
//        loginRequest.setUsername("john");
//        loginRequest.setPassword("password123");
//
//    }
//
//    /**
//     * TEST CASE:
//     * Should successfully register a new user when username and email are not taken
//     */
//    @Test
//    void testRegisterUser_success() {
//
//        // === Arrange ===
//        // Extract input values for readibility & consistency
//        String username = registerRequest.getUsername();
//        String email =  registerRequest.getEmail();
//        String rawPassword = registerRequest.getPassword();
//
//        // Mock repository behavior:
//        // - Username and email are not yet used
//        when(userRepository.existsByUsername(username)).thenReturn(false);
//        when(userRepository.existsByEmail(email)).thenReturn(false);
//
//        // Mock password hashing process
//        when(passwordEncoder.encode(rawPassword)).thenReturn("passwordEncoded");
//
//        // Mock saving entity to database (return the same onject)
//        when(userRepository.save(any(UserEntity.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        // === Act ===
//        // Execute the method under test
//        UserProfileResponse registerResponse = authService.register(registerRequest);
//
//        // === Assert ===
//        // Validate returned response is correct
//        assertNotNull(registerResponse);
//        assertEquals(registerResponse.getUsername(), username);
//        assertEquals(registerResponse.getEmail(), email);
//        assertEquals(Role.USER, registerResponse.getRole());
//
//        // Ensure user is active by default after registration
//        assertTrue(registerResponse.isActive());
//
//        // === Verify Interactions ===
//        // Ensure password was encoded beore saving
//        verify(passwordEncoder).encode(rawPassword);
//
//        // Ensure user is persisted to database exactly once
//        verify(userRepository, times(1)).save(any(UserEntity.class));
//    }
//
//    /**
//     * TEST CASE:
//     * Should throw exception when username already exists
//     */
//    @Test
//    void testRegisterUser_usernameAlreadyExists() {
//
//        // === Arrange ===
//        String username = registerRequest.getUsername();
//
//        // Mock that username already exists in database
//        when(userRepository.existsByUsername(username)).thenReturn(true);
//
//        // === Act & Assert ===
//        // Expect BadRequestException to be thrown
//        BadRequestException ex = assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
//        // Validate error message
//        assertEquals("username already exists", ex.getMessage());
//
//        // Ensure no user is saved due to validation failure
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
//
//    /**
//     * TEST CASE:
//     * Should throw exception when email already exists
//     */
//    @Test
//    void testRegisterUser_emailAlreadyExists() {
//        // === Arrange ===
//        String username = registerRequest.getUsername();
//        String email =  registerRequest.getEmail();
//
//        // Mock:
//        // - Username is available
//        // - Email is already taken
//        when(userRepository.existsByUsername(username)).thenReturn(false);
//        when(userRepository.existsByEmail(email)).thenReturn(true);
//
//        // === Act & Assert ===
//        BadRequestException ex = assertThrows(BadRequestException.class, () -> authService.register(registerRequest));
//        // Validate error message
//        assertEquals("email already exists", ex.getMessage());
//
//        // Ensure no user is saved due to validation failure
//        verify(userRepository, never()).save(any(UserEntity.class));
//    }
//
//    /**
//     * TEST CASE:
//     * Should successfully login when username and password are correct
//     */
//    @Test
//    void testLoginUser_success() {
//        // === Arrange ===
//        String username = loginRequest.getUsername();
//        String rawPassword = loginRequest.getPassword();
//
//        UserEntity user = new UserEntity(
//                "john",
//                "john@gmail.com",
//                "passwordEncoded",
//                Role.USER
//        );
//
//        // Mock user repository behavior
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(rawPassword, user.getPasswordHash())).thenReturn(true);
//
//        // Mock jwt service behavior
//        doReturn("access-token-123")
//                .when(jwtService)
//                .generateAccessToken(any(), any());
//        doReturn("refresh-token-456")
//                .when(jwtService)
//                .generateRefreshToken(any());
//
//        // Mock refresh token repository
//        doNothing().when(refreshTokenRepository).revokeAllByUser(user);
//        // Mock saving refresh token to database
//        when(refreshTokenRepository.save(any(RefreshTokenEntity.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        // === Act ===
//        // Execute the method under test
//        LoginResponse response = authService.login(loginRequest);
//
//        // === Assert ===
//        // Validate returned response is correct
//        assertNotNull(response);
//        assertEquals("access-token-123", response.getAccessToken());
//        assertEquals("refresh-token-456", response.getRefreshToken());
//        assertEquals("Bearer", response.getTokenType());
//
//        // === Verify Interactions ===
//        verify(userRepository).findByUsername(username);
//        verify(passwordEncoder).matches(rawPassword, user.getPasswordHash());
//        verify(jwtService, times(1)).generateAccessToken(any(), eq("USER"));
//        verify(jwtService, times(1)).generateRefreshToken(any());
//        verify(refreshTokenRepository, times(1)).revokeAllByUser(user);
//        verify(refreshTokenRepository, times(1)).save(any(RefreshTokenEntity.class));
//    }
//
//    /**
//     * TEST CASE:
//     * Should throw exception when username unauthorized
//     */
//    @Test
//    void testLoginUser_usernameUnauthorized() {
//
//        // === Arrange ===
//        String username = loginRequest.getUsername();
//
//        // Mock repository
//        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
//
//        // === Act & Assert ===
//        // Expect UnauthorizedException to be thrown
//        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
//
//        // Validate error message
//        assertEquals("invalid username or password", ex.getMessage());
//
//        // === Verify Interactions ===
//        verify(refreshTokenRepository, never()).save(any(RefreshTokenEntity.class));
//    }
//
//    /**
//     * TEST CASE:
//     * Should throw exception when user inactive
//     */
//    @Test
//    void testLogiUser_userInactive() {
//
//        // === Arrange ===
//        String username = loginRequest.getUsername();
//
//        UserEntity user = new UserEntity(
//                "john",
//                "john@gmail.com",
//                "hashedpassword",
//                Role.USER
//        );
//        // set user inactive
//        user.disable();
//
//        // Mock repository
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//
//        // === Act & Assert ===
//        // Expect UnauthorizedException to be thrown
//        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
//
//        // Validate error message
//        assertEquals("invalid username or password", ex.getMessage());
//
//        // === Verify Interactions ===
//        verify(refreshTokenRepository, never()).save(any(RefreshTokenEntity.class));
//    }
//
//    /**
//     * TEST CASE:
//     * Should throw exception when password mismatch
//     */
//    @Test
//    void testLogin_passwordMismatch() {
//
//        // === Arrange ===
//        String username = loginRequest.getUsername();
//        String rawPassword = loginRequest.getPassword();
//
//        UserEntity user = new UserEntity(
//                "john",
//                "john@gmail.com",
//                "hashedpassword",
//                Role.USER
//        );
//
//        // Mock repository
//        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(rawPassword, user.getPasswordHash())).thenReturn(false);
//
//        // === Act & Assert ===
//        // Expect UnauthorizedException to be thrown
//        UnauthorizedException ex = assertThrows(UnauthorizedException.class, () -> authService.login(loginRequest));
//
//        // Validate error message
//        assertEquals("invalid username or password", ex.getMessage());
//
//        // === Verify Interactions ===
//        verify(refreshTokenRepository, never()).save(any(RefreshTokenEntity.class));
//    }
//
//    /**
//     * TEST CASE:
//     * Should successfully refresh token when refresh token is valid
//     */
//    @Test
//    void testRefreshToken_success() {
//
//        // === Arrange ===
//        String rawToken = "valid-refresh-token";
//        String hashedToken = HashHelper.sha256(rawToken);
//
//        RefreshTokenRequest request = new RefreshTokenRequest();
//        request.setRefreshToken(rawToken);
//
//        UserEntity user = new UserEntity(
//                "john",
//                "john@gmail.com",
//                "passwordEncoded",
//                Role.USER
//        );
//
//        RefreshTokenEntity token = RefreshTokenEntity.createRefreshToken(
//                user,
//                hashedToken,
//                Instant.now().plus(1, ChronoUnit.DAYS) // masih aktif
//        );
//
//        // Mock repository
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(hashedToken))
//                .thenReturn(Optional.of(token));
//
//        // Mock JWT
//        doReturn("new-access-token")
//                .when(jwtService)
//                .generateAccessToken(any(), any());
//
//        doReturn("new-refresh-token")
//                .when(jwtService)
//                .generateRefreshToken(any());
//
//        when(refreshTokenRepository.save(any(RefreshTokenEntity.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
//
//        // === Act ===
//        LoginResponse response = authService.refresh(request);
//
//        // === Assert ===
//        assertNotNull(response);
//        assertEquals("new-access-token", response.getAccessToken());
//        assertEquals("new-refresh-token", response.getRefreshToken());
//        assertEquals("Bearer", response.getTokenType());
//
//        // === Verify ===
//        verify(refreshTokenRepository).findByTokenHashAndRevokedIsFalse(hashedToken);
//        verify(jwtService).generateAccessToken(any(), eq("USER"));
//        verify(jwtService).generateRefreshToken(any());
//        verify(refreshTokenRepository).save(any(RefreshTokenEntity.class));
//
//        // pastikan token lama di revoke
//        assertTrue(token.isRevoked());
//    }
//
//    /**
//     * TEST CASE:
//     * Should throw exception when refresh token is invalid
//     */
//    @Test
//    void testRefreshToken_invalidToken() {
//
//        // === Arrange ===
//        String rawToken = "invalid-token";
//        String hashedToken = HashHelper.sha256(rawToken);
//
//        RefreshTokenRequest request = new RefreshTokenRequest();
//        request.setRefreshToken(rawToken);
//
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(hashedToken))
//                .thenReturn(Optional.empty());
//
//        // === Act & Assert ===
//        UnauthorizedException ex = assertThrows(
//                UnauthorizedException.class,
//                () -> authService.refresh(request)
//        );
//
//        assertEquals("invalid refresh token", ex.getMessage());
//
//        // === Verify ===
//        verify(refreshTokenRepository, never()).save(any());
//    }
//
//    /**
//     * TEST CASE:
//     * Should successfully logout and revoke refresh token
//     */
//    @Test
//    void testLogoutUser_success() {
//
//        // === Arrange ===
//        String rawToken = "valid-token";
//        String hashedToken = HashHelper.sha256(rawToken);
//
//        RefreshTokenRequest request = new RefreshTokenRequest();
//        request.setRefreshToken(rawToken);
//
//        UserEntity user = new UserEntity(
//                "john",
//                "john@gmail.com",
//                "passwordEncoded",
//                Role.USER
//        );
//
//        RefreshTokenEntity token = RefreshTokenEntity.createRefreshToken(
//                user,
//                hashedToken,
//                Instant.now().plus(1, ChronoUnit.DAYS)
//        );
//
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(hashedToken))
//                .thenReturn(Optional.of(token));
//
//        // === Act ===
//        authService.logout(request);
//
//        // === Assert ===
//        assertTrue(token.isRevoked());
//
//        // === Verify ===
//        verify(refreshTokenRepository).findByTokenHashAndRevokedIsFalse(hashedToken);
//    }
//
//    /**
//     * TEST CASE:
//     * Should successfully logout and revoke refresh token
//     */
//    @Test
//    void testLogout_success() {
//
//        // === Arrange ===
//        String rawToken = "valid-token";
//        String hashedToken = HashHelper.sha256(rawToken);
//
//        RefreshTokenRequest request = new RefreshTokenRequest();
//        request.setRefreshToken(rawToken);
//
//        UserEntity user = new UserEntity(
//                "john",
//                "john@gmail.com",
//                "passwordEncoded",
//                Role.USER
//        );
//
//        RefreshTokenEntity token = RefreshTokenEntity.createRefreshToken(
//                user,
//                hashedToken,
//                Instant.now().plus(1, ChronoUnit.DAYS)
//        );
//
//        when(refreshTokenRepository.findByTokenHashAndRevokedIsFalse(hashedToken))
//                .thenReturn(Optional.of(token));
//
//        // === Act ===
//        authService.logout(request);
//
//        // === Assert ===
//        assertTrue(token.isRevoked());
//
//        // === Verify ===
//        verify(refreshTokenRepository).findByTokenHashAndRevokedIsFalse(hashedToken);
//    }
//}