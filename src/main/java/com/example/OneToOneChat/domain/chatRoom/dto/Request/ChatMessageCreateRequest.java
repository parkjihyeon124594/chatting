package com.example.OneToOneChat.domain.chatRoom.dto.Request;

import lombok.Builder;

@Builder
public record ChatMessageCreateRequest(
        String writer,
        String content
) {
    public static ChatMessageCreateRequest make(String writer,String content){
        return ChatMessageCreateRequest.builder()
                .content(content)
                .writer(writer).build();
    }
}
