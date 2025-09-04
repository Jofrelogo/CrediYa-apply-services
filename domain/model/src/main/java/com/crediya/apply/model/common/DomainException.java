package com.crediya.apply.model.common;

public abstract class DomainException extends RuntimeException {
    private final ErrorCode code;

    public DomainException(String message, ErrorCode code) {
        super(message);
        this.code = code;
    }

    public ErrorCode getErrorCode() {
        return code;
    }

}
