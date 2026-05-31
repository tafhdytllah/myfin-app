package com.tafh.myfin_app.user.controller;

import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.util.ResponseHelper;
import com.tafh.myfin_app.user.dto.UpdateUserProfileRequest;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getCurrentUser() {
        return ResponseHelper.ok(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserProfileResponse>> update(
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        return ResponseHelper.ok(userService.update(request));
    }

}
