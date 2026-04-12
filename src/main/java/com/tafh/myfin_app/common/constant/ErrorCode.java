package com.tafh.myfin_app.common.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("Something went wrong"),
    UNAUTHORIZED("Invalid credentials"),
    FORBIDDEN("Access denied"),
    VALIDATION_ERROR("Invalid request"),
    BAD_REQUEST("Bad request"),
    NOT_FOUND("Resource not found"),
    USER_ALREADY_EXISTS("User already exists");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
