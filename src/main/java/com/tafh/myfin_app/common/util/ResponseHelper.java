package com.tafh.myfin_app.common.util;

import com.tafh.myfin_app.common.constant.ErrorCode;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.dto.ErrorResponse;
import com.tafh.myfin_app.common.dto.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@RequiredArgsConstructor
public class ResponseHelper {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponse.success(data, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, PagingResponse paging) {
        return ResponseEntity.ok(ApiResponse.success(data, paging));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(data));
    }

    /**
     * FOR VALIDATION ERROR
     */
    public static ResponseEntity<ApiResponse<Object>> error(
            HttpStatus status,
            ErrorCode code,
            Map<String, String> details
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(code.name())
                .message(code.getMessage())
                .details(details)
                .build();

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(errorResponse));
    }

    /**
     * FOR GENERAL / BUSINESS ERROR
     */
    public static ResponseEntity<ApiResponse<Object>> error(
            HttpStatus status,
            ErrorCode code,
            String message
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(code.name())
                .message(message)
                .details(null)
                .build();

        return ResponseEntity
                .status(status)
                .body(ApiResponse.error(errorResponse));
    }

}
