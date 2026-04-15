package com.tafh.myfin_app.user.service;

import com.tafh.myfin_app.common.exception.UnauthorizedException;
import com.tafh.myfin_app.common.security.SecurityUtil;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.model.Role;
import com.tafh.myfin_app.user.mapper.UserMapper;
import com.tafh.myfin_app.user.model.UserEntity;
import com.tafh.myfin_app.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;

    /**
     * GET CURRENT USER
     */
    @Test
    void shouldReturnCurrentUser_success() {

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUserId).thenReturn("user-1");

            UserEntity user = mock(UserEntity.class);

            UserProfileResponse response = UserProfileResponse.builder()
                    .id("user-1")
                    .username("john")
                    .email("john@gmail.com")
                    .role(Role.USER)
                    .isActive(true)
                    .build();

            when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
            when(userMapper.toUserProfileResponse(user)).thenReturn(response);

            UserProfileResponse result = userService.getCurrentUser();

            assertNotNull(result);
            assertEquals("user-1", result.getId());
            assertEquals("john", result.getUsername());

            verify(userRepository, times(1)).findById("user-1");
            verify(userMapper, times(1)).toUserProfileResponse(user);
        }
    }

    @Test
    void shouldThrow_whenUserNotFound() {
        try (MockedStatic<SecurityUtil> mocked =
                     mockStatic(SecurityUtil.class)) {

            mocked.when(SecurityUtil::getCurrentUserId)
                    .thenReturn("user-1");

            when(userRepository.findById("user-1")).thenReturn(Optional.empty());

            assertThrows(UnauthorizedException.class,
                    () -> userService.getCurrentUser());
        }
    }

    /**
     * FIND BY USERNAME
     */
    @Test
    void shouldReturnUser_whenUsernameValid() {

        String username = "john";

        UserEntity user = mock(UserEntity.class);

        when(user.getUsername()).thenReturn(username);

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));

        UserEntity result = userService.findByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());

        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void shouldThrow_whenUsernameInvalid() {

        String username = "wrong";

        when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class,
                () -> userService.findByUsername(username));

        verify(userRepository, times(1)).findByUsername(username);
    }

}