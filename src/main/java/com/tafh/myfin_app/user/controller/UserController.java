package com.tafh.myfin_app.user.controller;

import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser() {
        UserProfileResponse user = userService.getCurrentUser();

        return ResponseEntity.ok(ApiResponse.success(user));
    }
}
