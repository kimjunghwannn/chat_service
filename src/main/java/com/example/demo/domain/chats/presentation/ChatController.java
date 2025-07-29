package com.example.demo.domain.chats.presentation;

import com.example.demo.domain.chats.application.ChatService;
import com.example.demo.domain.chats.dto.ChatMessageDto;
import com.example.demo.domain.chats.dto.CreateChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController
{
    private final ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody ChatMessageDto chatMessageDto)
    {
        chatService.sendChatMessage(chatMessageDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Long> createChatRoom(@RequestBody CreateChatDto createChatDto)
    {
        return ResponseEntity.ok(chatService.createChatRoom(createChatDto));
    }
}
