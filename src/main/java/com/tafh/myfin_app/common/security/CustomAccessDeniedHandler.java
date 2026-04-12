package com.tafh.myfin_app.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.dto.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> body = ApiResponse.error(ErrorResponse.builder()
                .code("FORBIDDEN")
                .message("Access Denied")
                .build());

        objectMapper.writeValue(response.getOutputStream(), body);
    }

}
