package com.example.demo.domain.chats.dto;

public record ChatMessageDto(
        Long chatRoomId,
        String payload,
        String senderName
) {
}
