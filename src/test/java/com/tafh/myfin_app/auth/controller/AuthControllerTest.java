//package com.tafh.myfin_app.auth.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.tafh.myfin_app.auth.dto.request.LoginRequest;
//import com.tafh.myfin_app.auth.dto.request.RefreshTokenRequest;
//import com.tafh.myfin_app.auth.dto.request.RegisterRequest;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//class AuthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // =======================
//    // REGISTER
//    // =======================
//
//    /**
//     * TEST CASE:
//     * Should successfully register user
//     */
//    @Test
//    void shouldRegisterUserSuccessfully() throws Exception {
//
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("john");
//        request.setEmail("john@gmail.com");
//        request.setPassword("password123");
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.username").value("john"))
//                .andExpect(jsonPath("$.data.email").value("john@gmail.com"))
//                .andExpect(jsonPath("$.data.role").value("USER"))
//                .andExpect(jsonPath("$.data.active").value(true));
//    }
//
//    /**
//     * TEST CASE:
//     * Should fail when username already exists
//     */
//    @Test
//    void shouldFailRegister_whenUsernameAlreadyExists() throws Exception {
//
//        RegisterRequest request = new RegisterRequest();
//        request.setUsername("john");
//        request.setEmail("john@gmail.com");
//        request.setPassword("password123");
//
//        // first register
//        mockMvc.perform(post("/api/v1/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)));
//
//        // second register
//        mockMvc.perform(post("/api/v1/auth/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.errors.code").value("VALIDATION_ERROR"));
//    }
//
//    // =======================
//    // LOGIN
//    // =======================
//
//    /**
//     * TEST CASE:
//     * Should login successfully
//     */
//    @Test
//    void shouldLoginSuccessfully() throws Exception {
//
//        // register dulu
//        RegisterRequest register = new RegisterRequest();
//        register.setUsername("john");
//        register.setEmail("john@gmail.com");
//        register.setPassword("password123");
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(register)));
//
//        // login
//        LoginRequest login = new LoginRequest();
//        login.setUsername("john");
//        login.setPassword("password123");
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(login)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.data.accessToken").exists())
//                .andExpect(jsonPath("$.data.refreshToken").exists())
//                .andExpect(jsonPath("$.data.tokenType").value("Bearer"));
//    }
//
//    /**
//     * TEST CASE:
//     * Should fail login when password wrong
//     */
//    @Test
//    void shouldFailLogin_whenPasswordWrong() throws Exception {
//
//        // register dulu
//        RegisterRequest register = new RegisterRequest();
//        register.setUsername("john");
//        register.setEmail("john@gmail.com");
//        register.setPassword("password123");
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(register)));
//
//        // login salah password
//        LoginRequest login = new LoginRequest();
//        login.setUsername("john");
//        login.setPassword("wrong");
//
//        mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(login)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.errors.code").value("UNAUTHORIZED"));
//    }
//
//    // =======================
//    // REFRESH + LOGOUT FLOW
//    // =======================
//
//    /**
//     * TEST CASE:
//     * Should refresh token successfully
//     */
//    @Test
//    void shouldRefreshTokenSuccessfully() throws Exception {
//
//        // 1. register
//        RegisterRequest register = new RegisterRequest();
//        register.setUsername("john");
//        register.setEmail("john@gmail.com");
//        register.setPassword("password123");
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(register)));
//
//        // 2. login
//        LoginRequest login = new LoginRequest();
//        login.setUsername("john");
//        login.setPassword("password123");
//
//        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(login)))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        String refreshToken = objectMapper
//                .readTree(loginResponse)
//                .path("data")
//                .path("refreshToken")
//                .asText();
//
//        // 3. refresh
//        RefreshTokenRequest refresh = new RefreshTokenRequest();
//        refresh.setRefreshToken(refreshToken);
//
//        mockMvc.perform(post("/api/v1/auth/refresh")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(refresh)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.accessToken").exists())
//                .andExpect(jsonPath("$.data.refreshToken").exists());
//    }
//
//    /**
//     * TEST CASE:
//     * Should logout successfully
//     */
//    @Test
//    void shouldLogoutSuccessfully() throws Exception {
//
//        // register + login dulu
//        RegisterRequest register = new RegisterRequest();
//        register.setUsername("john");
//        register.setEmail("john@gmail.com");
//        register.setPassword("password123");
//
//        mockMvc.perform(post("/api/v1/auth/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(register)));
//
//        LoginRequest login = new LoginRequest();
//        login.setUsername("john");
//        login.setPassword("password123");
//
//        String loginResponse = mockMvc.perform(post("/api/v1/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(login)))
//                .andReturn()
//                .getResponse()
//                .getContentAsString();
//
//        String refreshToken = objectMapper
//                .readTree(loginResponse)
//                .path("data")
//                .path("refreshToken")
//                .asText();
//
//        // logout
//        RefreshTokenRequest logout = new RefreshTokenRequest();
//        logout.setRefreshToken(refreshToken);
//
//        mockMvc.perform(post("/api/v1/auth/logout")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(logout)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("logout success"));
//    }
//}