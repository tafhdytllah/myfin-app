package com.tafh.myfin_app.auth.dto.internal;

import com.tafh.myfin_app.auth.dto.response.LoginResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class LoginResult {

    private LoginResponse response;

    private String refreshToken;

}
