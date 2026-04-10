package com.tafh.myfin_app.auth.dto.internal;

import com.tafh.myfin_app.user.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class RotateTokenResult {

    private UserEntity user;

    private String newRefreshToken;

}
