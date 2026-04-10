package com.tafh.myfin_app.user.mapper;

import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.enums.Role;
import com.tafh.myfin_app.user.model.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileResponse toUserProfileResponse(UserEntity user) {
        if (user == null) return null;

        return UserProfileResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .isActive(user.getIsActive())
                .build();
    }

    public UserEntity toUserEntity(String username, String email, String hashedPassword, Role role) {
        return new UserEntity(
                username,
                email,
                hashedPassword,
                role
        );
    }

}
