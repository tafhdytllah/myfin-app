package com.tafh.myfin_app.user.mapper;

import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileResponse toUserProfileResponse(UserEntity user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .build();
    }

}
