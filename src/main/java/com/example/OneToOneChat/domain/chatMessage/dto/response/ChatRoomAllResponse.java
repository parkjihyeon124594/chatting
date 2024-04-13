package com.example.OneToOneChat.domain.chatMessage.dto.response;

import com.example.OneToOneChat.domain.chatMessage.entity.ChatRoom;
import lombok.Builder;

@Builder
public record ChatRoomAllResponse(
        String clientName,
        Long id
) {
    public static ChatRoomAllResponse makeAll(ChatRoom chatRoom){
        return ChatRoomAllResponse.builder()
                .clientName(chatRoom.getChatRoomName())
                .id(chatRoom.getChatRoomId())
                .build();
    }
}
