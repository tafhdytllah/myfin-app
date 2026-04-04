package com.tafh.myfin_app.health.controller;

import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.util.LogHelper;
import com.tafh.myfin_app.common.util.ResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@RequiredArgsConstructor
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<String>> checkHealth(HttpServletRequest request) {
        String endpoint = request.getMethod() + " " + request.getRequestURI();
        LogHelper.request(endpoint, null);

        ResponseEntity<ApiResponse<String>> response = ResponseHelper.ok("Server is running");

        LogHelper.response(endpoint, response);
        return response;
    }
}
