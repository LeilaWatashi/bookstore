package com.watashi.bookstore.exception;

import lombok.Getter;

@Getter
public enum ErrorPageType {
    UNAUTHORIZED("pages/error/401"),
    ACCESS_DENIED("pages/error/403"),
    NOT_FOUND("pages/error/404"),
    INTERNAL_SERVER_ERROR("pages/error/500");

    private final String path;

    ErrorPageType(String path) {
        this.path = path;
    }
}
