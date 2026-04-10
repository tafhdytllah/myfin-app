package com.tafh.myfin_app.auth.controller;

import com.tafh.myfin_app.auth.dto.internal.LoginResult;
import com.tafh.myfin_app.auth.dto.internal.RefreshResult;
import com.tafh.myfin_app.auth.dto.request.LoginRequest;
import com.tafh.myfin_app.auth.dto.request.RegisterRequest;
import com.tafh.myfin_app.auth.dto.response.LoginResponse;
import com.tafh.myfin_app.auth.service.AuthService;
import com.tafh.myfin_app.common.cookie.CookieUtil;
import com.tafh.myfin_app.config.properties.CookieProperties;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieProperties cookieProperties;
    private final CookieUtil cookieUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserProfileResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseHelper.created(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response
    ) {
        LoginResult result = authService.login(request);

        cookieUtil.addRefreshTokenCookie(response, result.getRefreshToken());

        return ResponseHelper.ok(result.getResponse());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {

        RefreshResult result = authService.refreshAccessToken(refreshToken);

        cookieUtil.addRefreshTokenCookie(response, result.getRefreshToken());

        return ResponseHelper.ok(result.getResponse());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response
    ) {
        authService.logout(refreshToken);

        cookieUtil.deleteRefreshTokenCookie(response);

        return ResponseHelper.ok(null, "Logged out successfully");
    }

}
