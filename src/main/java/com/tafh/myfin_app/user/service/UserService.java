package com.tafh.myfin_app.user.service;

import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.common.security.CurrentUser;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.mapper.UserMapper;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CurrentUser currentUser;

    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUser() {
        String userId = currentUser.getId();

        UserEntity user = userRepository
                .findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Invalid authentication"));

        return userMapper.toUserProfileResponse(user);
    }

}
