package com.tafh.myfin_app.common.util;

import com.tafh.myfin_app.common.constant.ErrorCode;
import com.tafh.myfin_app.common.dto.ApiResponse;
import com.tafh.myfin_app.common.dto.ErrorResponse;
import com.tafh.myfin_app.common.dto.PagingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
public class ResponseHelper {

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, PagingResponse paging) {
        return ResponseEntity.ok(ApiResponse.success(data, paging));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(data));
    }

    public static ResponseEntity<ApiResponse<Object>> error(
            HttpStatus status,
            ErrorCode code,
            Object details
    ) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(String.valueOf(code))
                .message(code.getMessage())
                .details(details)
                .build();

        return ResponseEntity.status(status)
                .body(ApiResponse.error(errorResponse));
    }

}
