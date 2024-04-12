package com.example.OneToOneChat.domain.dto.Response;

import com.example.OneToOneChat.domain.entity.ChatMessage;
import lombok.Builder;

@Builder
public class ChatRoomAllResponse {

    private String clientName;
    public static ChatRoomAllResponse makeAll(ChatMessage chatMessage){
        return ChatRoomAllResponse.builder()
                .clientName(chatMessage.getWriter())
                .build();
    }
}
