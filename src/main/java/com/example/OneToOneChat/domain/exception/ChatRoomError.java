package com.example.OneToOneChat.domain.exception;

import com.example.OneToOneChat.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public enum ChatRoomError implements ErrorCode {

    CHAT_ROOM_NOT_FOUND (HttpStatus.NOT_FOUND,"Noneexistent chatRoom");
    private final HttpStatus status;
    private final String message;

    ChatRoomError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }


    @Override
    public HttpStatus status() {
        return this.status;
    }

    @Override
    public String code() {
        return this.name();
    }

    @Override
    public String message() {
        return this.message;
    }

}
