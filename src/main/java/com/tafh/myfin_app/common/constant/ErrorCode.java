package com.tafh.myfin_app.common.constant;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // ========================
    // GENERAL
    // ========================
    INTERNAL_SERVER_ERROR("Something went wrong"),

    // ========================
    // AUTH
    // ========================
    UNAUTHORIZED("Invalid credentials"),
    FORBIDDEN("Access denied"),

    // ========================
    // VALIDATION
    // ========================
    VALIDATION_ERROR("Invalid request"),
    BAD_REQUEST("Bad request"),

    // ========================
    // RESOURCE
    // ========================
    NOT_FOUND("Resource not found"),
    USER_ALREADY_EXISTS("User already exists");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
