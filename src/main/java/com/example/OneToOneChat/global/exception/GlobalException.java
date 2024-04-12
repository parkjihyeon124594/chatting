package com.example.OneToOneChat.global.exception;

import org.springframework.http.HttpStatus;

public class GlobalException extends RuntimeException{
    private final HttpStatus status;
    private final String code;
    private final String message;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.message());
        this.status = errorCode.status();
        this.code = errorCode.code();
        this.message = errorCode.message();
    }
}
