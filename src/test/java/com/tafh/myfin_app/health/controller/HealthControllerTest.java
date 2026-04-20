//package com.tafh.myfin_app.health.controller;
//
//import com.tafh.myfin_app.common.security.JwtAuthenticationFilter;
//import com.tafh.myfin_app.config.TestSecurityConfig;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(
//        controllers = HealthController.class,
//        excludeFilters = @ComponentScan.Filter(
//                type = FilterType.ASSIGNABLE_TYPE,
//                classes = JwtAuthenticationFilter.class
//        )
//)
//@Import(TestSecurityConfig.class)
//class HealthControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void healthCheck_shouldReturn200() throws Exception {
//        mockMvc.perform(get("/health"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Server is running"));
//    }
//
//}