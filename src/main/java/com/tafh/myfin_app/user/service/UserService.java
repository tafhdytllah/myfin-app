package com.tafh.myfin_app.user.service;

import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity verifyUserLogin(String username, String rawPassword) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("invalid username or password"));

        if (!user.getIsActive() || !passwordEncoder.matches(rawPassword, user.getPasswordHash()))
            throw new UnauthorizedException("invalid username or password");

        return user;
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("invalid username"));
    }
}
