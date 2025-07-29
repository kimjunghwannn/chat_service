package com.example.demo.domain.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private Long chatRoomId;
    private String message;
    private boolean isHotChat;
}