package com.tafh.myfin_app.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tafh.myfin_app.user.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserProfileResponse {

    private String id;

    private String username;

    private String email;

    private Role role;

    private boolean isActive;
}
