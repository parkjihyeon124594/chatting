package com.example.OneToOneChat.domain.dto.Request;

public record ChatMessageCreateRequest(
        String writer,
        String content
) {
}
