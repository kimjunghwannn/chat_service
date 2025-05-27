package com.example.demo;

import com.example.demo.domain.global.ChatHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChatServiceTest {

    @Mock
    private WebSocketSession session;
    @InjectMocks
    private ChatHandler chatHandler;

    @BeforeEach
    void setUp() {

        Long chatRoomId = 1L;

        // Mock WebSocketSession 생성
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("chatRoomId", chatRoomId);

        when(session.getAttributes()).thenReturn(attributes);
        when(session.isOpen()).thenReturn(true);


        chatHandler.afterConnectionEstablished(session);
    }

    @Test
    void testHotChatMessagesAreFlushedCorrectly() throws Exception {
        Long chatRoomId = 1L;

        // HotChat 메시지를 여러 개 입력
        chatHandler.handleTextMessage(chatRoomId, "message1", true);
        chatHandler.handleTextMessage(chatRoomId, "message2", true);
        chatHandler.handleTextMessage(chatRoomId, "message3", true);

        // 1.1초 대기 (scheduler가 flush를 수행하도록)
        Thread.sleep(1100);

        chatHandler.handleTextMessage(chatRoomId, "message1", true);
        chatHandler.handleTextMessage(chatRoomId, "message2", true);
        chatHandler.handleTextMessage(chatRoomId, "message3", true);
        chatHandler.handleTextMessage(chatRoomId, "message1", true);
        Thread.sleep(900);
        chatHandler.handleTextMessage(chatRoomId, "message1", true);
        chatHandler.handleTextMessage(chatRoomId, "message2", true);
        chatHandler.handleTextMessage(chatRoomId, "message3", true);

        Thread.sleep(1100);




    }

    @Test
    void concurrencyFlushTest() {
        Long chatRoomId = 1L;

        // 여러 메시지 버퍼에 추가 (예: 100개)
        for (int i = 0; i < 100; i++) {
            chatHandler.addHotChatMessage(chatRoomId, "msg" + i);
        }

        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    chatHandler.flushHotChatMessages(chatRoomId);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}