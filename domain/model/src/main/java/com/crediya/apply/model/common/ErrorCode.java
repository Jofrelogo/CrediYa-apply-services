package com.crediya.apply.model.common;

public enum ErrorCode {
    USER_NOT_FOUND("USR_404_001"),
    EMAIL_ALREADY_EXISTS("USR_409_001"),
    VALIDATION_ERROR("GEN_400_001"),
    GENERIC_ERROR("GEN_500_001");

    private final String value;
    ErrorCode(String value) { this.value = value; }
    public String value() { return value; }
}

