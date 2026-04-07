package com.tafh.myfin_app.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {
    private final String field;

    public BadRequestException(String field, String message) {
        super(message);
        this.field = field;
    }

}
