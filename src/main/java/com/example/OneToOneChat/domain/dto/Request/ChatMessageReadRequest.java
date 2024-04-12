package com.example.OneToOneChat.domain.dto.Request;

public record ChatMessageReadRequest(
        String counselor,
        String client
) {
}
