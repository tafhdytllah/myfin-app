package com.tafh.myfin_app.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tafh.myfin_app.auth.dto.request.LoginRequest;
import com.tafh.myfin_app.auth.dto.request.RegisterRequest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * INTEGRATION TEST AUTH CONTROLLER
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * REGISTER
     */
    @Test
    void shouldRegisterUserSuccessfully() throws Exception {
        String unique = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john"+unique);
        request.setEmail("john"+unique+"@gmail.com");
        request.setPassword("passworD123@");

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.username").value("john"+unique))
                .andExpect(jsonPath("$.data.email").value("john"+unique+"@gmail.com"))
                .andExpect(jsonPath("$.data.role").value("USER"))
                .andExpect(jsonPath("$.data.active").value(true));
    }

    @Test
    void shouldFailRegister_whenUsernameAlreadyExists() throws Exception {
        String unique = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john"+unique);
        request.setEmail("john"+unique+"@gmail.com");
        request.setPassword("passworD123@");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.code").value("VALIDATION_ERROR"));
    }

    /**
     * LOGIN
     */
    @Test
    void shouldLoginSuccessfully() throws Exception {
        String unique = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        RegisterRequest register = new RegisterRequest();
        register.setUsername("john"+unique);
        register.setEmail("john"+unique+"@gmail.com");
        register.setPassword("passworD123@");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

        LoginRequest login = new LoginRequest();
        login.setUsername("john"+unique);
        login.setPassword("passworD123@");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    void shouldFailLogin_whenPasswordWrong() throws Exception {
        String unique = UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        RegisterRequest register = new RegisterRequest();
        register.setUsername("john"+unique);
        register.setEmail("john"+unique+"@gmail.com");
        register.setPassword("passworD123@");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

        LoginRequest login = new LoginRequest();
        login.setUsername("john"+unique);
        login.setPassword("wrong");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors.code").value("UNAUTHORIZED"));
    }

    @Test
    void shouldFailLogin_whenUserNotFound() throws Exception {

        LoginRequest login = new LoginRequest();
        login.setUsername("ghost123");
        login.setPassword("passworD123@");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors.code").value("UNAUTHORIZED"));
    }

    /**
     * REFRESH
     */
    @Test
    void shouldRefreshTokenSuccessfully() throws Exception {

        MvcResult loginResult = registerAndLogin();

        String refreshToken = extractRefreshToken(loginResult);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").exists());
    }

    @Test
    void shouldFailRefresh_whenCookieMissing() throws Exception {

        mockMvc.perform(post("/api/v1/auth/refresh"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors.code").value("UNAUTHORIZED"));
    }

    @Test
    void shouldFailRefresh_whenTokenInvalid() throws Exception {

        mockMvc.perform(post("/api/v1/auth/refresh")
                .cookie(new Cookie("refreshToken", "invalid-token")))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors.code").value("UNAUTHORIZED"));
    }

    /**
     * LOGOUT
     */
    @Test
    void shouldLogoutSuccessfully() throws Exception {

        MvcResult loginResult = registerAndLogin();

        String refreshToken = extractRefreshToken(loginResult);

        mockMvc.perform(post("/api/v1/auth/logout")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"));
    }

    @Test
    void shouldFailLogout_whenCookieMissing() throws Exception {

        mockMvc.perform(post("/api/v1/auth/logout"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.errors.code").value("UNAUTHORIZED"));
    }

    /**
     * HELPER METHOD
     */
    private MvcResult registerAndLogin() throws Exception {
        String unique = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        RegisterRequest register = new RegisterRequest();
        register.setUsername("john"+unique);
        register.setEmail("john"+unique+"@gmail.com");
        register.setPassword("passworD123@");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

        LoginRequest login = new LoginRequest();
        login.setUsername("john"+unique);
        login.setPassword("passworD123@");

        return mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andReturn();
    }

    private String extractRefreshToken(MvcResult result) {
        String setCookie = result.getResponse().getHeader("Set-Cookie");

        Assertions.assertNotNull(setCookie);
        return Arrays.stream(setCookie.split(";"))
                .filter(c -> c.trim().startsWith("refreshToken="))
                .findFirst()
                .map(c -> c.substring("refreshToken=".length()))
                .orElseThrow();
    }
}