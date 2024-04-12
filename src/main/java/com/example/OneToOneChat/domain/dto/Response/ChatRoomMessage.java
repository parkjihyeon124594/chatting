package com.example.OneToOneChat.domain.dto.Response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomMessage {
    private String writer;
    private String content;

    public ChatRoomMessage(String writer, String content){
        this.writer=writer;
        this.content=content;
    }
}
