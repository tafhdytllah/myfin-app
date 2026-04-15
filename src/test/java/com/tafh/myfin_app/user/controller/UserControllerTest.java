package com.tafh.myfin_app.user.controller;

import com.tafh.myfin_app.common.security.JwtService;
import com.tafh.myfin_app.config.TestSecurityConfig;
import com.tafh.myfin_app.user.dto.UserProfileResponse;
import com.tafh.myfin_app.user.model.Role;
import com.tafh.myfin_app.user.repository.UserRepository;
import com.tafh.myfin_app.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.when;


@WebMvcTest(controllers = UserController.class)
@Import(TestSecurityConfig.class)
class UserControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private UserService userService;
    @MockBean private UserRepository userRepository;
    @MockBean private JwtService jwtService;

    /**
     * SUCCESS /me
     */
    @Test
    @WithMockUser(username = "user-1", roles = "USER")
    void shouldReturnCurrentUser_success() throws Exception {

        UserProfileResponse response = UserProfileResponse.builder()
                .id("user-1")
                .username("john")
                .email("john@gmail.com")
                .role(Role.USER)
                .isActive(true)
                .build();

        when(userService.getCurrentUser()).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("user-1"))
                .andExpect(jsonPath("$.data.username").value("john"))
                .andExpect(jsonPath("$.data.email").value("john@gmail.com"));
    }

}