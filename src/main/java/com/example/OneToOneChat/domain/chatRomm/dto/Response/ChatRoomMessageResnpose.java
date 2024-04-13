package com.example.OneToOneChat.domain.chatRomm.dto.Response;

import com.example.OneToOneChat.domain.chatRomm.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatRoomMessageResnpose {
    private String writer;
    private String content;

    public ChatRoomMessageResnpose (String writer, String content){
        this.writer=writer;
        this.content=content;
    }

    public static ChatRoomMessageResnpose makeAll(ChatMessage chatMessage){
        return ChatRoomMessageResnpose.builder()
                .writer(chatMessage.getWriter())
                .content(chatMessage.getContent()).
                build();
    }
}
