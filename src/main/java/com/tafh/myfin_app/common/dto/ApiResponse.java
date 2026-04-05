package com.tafh.myfin_app.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private T data;
    private String message;
    private PagingResponse paging;
    private Object errors;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, PagingResponse paging) {
        return ApiResponse.<T>builder()
                .data(data)
                .paging(paging)
                .build();
    }

    public static ApiResponse<Object> error(ErrorResponse errors) {
        return ApiResponse.builder()
                .errors(errors)
                .build();
    }

}
