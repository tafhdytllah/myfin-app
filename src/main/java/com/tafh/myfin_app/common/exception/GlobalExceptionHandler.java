package com.tafh.myfin_app.common.exception;

import com.tafh.myfin_app.common.constant.ErrorCode;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.util.LogHelper;
import com.tafh.myfin_app.common.util.ResponseHelper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /* =======================
     VALIDATION ERROR
     ======================= */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> details = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(e ->
                        details.putIfAbsent(e.getField(), e.getDefaultMessage())
                );

        LogHelper.warn("BAD_REQUEST : {}", details);
        return ResponseHelper.error(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, details);
    }

    /* =======================
       BUSINESS ERRORS
       ======================= */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequest(
            BadRequestException ex
    ) {
        Map<String, String> details = new HashMap<>();
        details.putIfAbsent(ex.getField(), ex.getMessage());

        LogHelper.warn("BAD_REQUEST : {}", details);
        return ResponseHelper.error(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_ERROR, details);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnauthorized(
            UnauthorizedException ex
    ) {
        LogHelper.warn("UNAUTHORIZED : {}", ex.getMessage());
        return ResponseHelper.error(HttpStatus.UNAUTHORIZED, ErrorCode.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Object>> handleForbidden(
            ForbiddenException ex
    ) {
        LogHelper.warn("FORBIDDEN : {}", ex.getMessage());
        return ResponseHelper.error(HttpStatus.FORBIDDEN, ErrorCode.FORBIDDEN, null);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(
            ResourceNotFoundException ex
    ) {
        LogHelper.warn("NOT_FOUND : {}", ex.getMessage());
        return ResponseHelper.error(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, ex.getMessage());
    }

    /* =======================
       FALLBACK
       ======================= */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        LogHelper.error("INTERNAL_SERVER_ERROR : {}", ex.getMessage());
        return ResponseHelper.error(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR, null);
    }

}
