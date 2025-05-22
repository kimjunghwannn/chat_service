package com.example.demo.domain.global;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler
{
    private static final Map<Long, Set<WebSocketSession>> rooms = new HashMap<>();
    private final Map<Long, List<String>> hotChatMessageBuffer = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long chatRoomId = getChatRoomId(session);
        Long userId= getUserId(session);
        rooms.computeIfAbsent(chatRoomId, k -> new HashSet<>()).add(session);

        log.info("{} 연결됨", session.getId());
        log.info("{} 유저 연결됨", userId);
    }

    public void addHotChatMessage(Long chatRoomId, String message)
    {
        hotChatMessageBuffer.computeIfAbsent(chatRoomId, k -> new ArrayList<>()).add(message);
    }

    public void handleTextMessage(Long chatRoomId, String message,boolean isHotChat) throws IOException, InterruptedException {
        log.info("사용자로부터 메시지 수신: {}", message);
        if(isHotChat)
        {
            hotChatMessageBuffer.computeIfAbsent(chatRoomId, k -> Collections.synchronizedList(new ArrayList<>()))
                    .add(message);

            scheduler.schedule(() -> {
                try {
                    flushHotChatMessages(chatRoomId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, 1, TimeUnit.SECONDS);
        }

        else {
            broadcastMessage(chatRoomId,message);
        }

    }
    public void flushHotChatMessages(Long chatRoomId) throws IOException {
        List<String> messages = hotChatMessageBuffer.remove(chatRoomId);

        if (messages == null || messages.isEmpty()) {
            log.info("flush 실행 - chatRoomId: {}, 메시지 없음", chatRoomId);
            return;
        }

        log.info("flush 실행 - chatRoomId: {}, 메시지 수: {}", chatRoomId, messages.size());


       for (String message : messages)
           broadcastMessage(chatRoomId,message);
    }
    public void broadcastMessage(Long chatRoomId, String message) throws IOException {
        Set<WebSocketSession> sessions = rooms.get(chatRoomId);
        if (sessions == null) return;

        for (WebSocketSession session : sessions) {
            log.info("사용자에게 메시지를 보냅니다: {}     {}", message, session);
            sendMessage(session, message);
        }
    }

    public void sendMessage(WebSocketSession session, String message) throws IOException {
        synchronized (session) {
            if (session.isOpen())
            {
                session.sendMessage(new TextMessage(message));
            }
        }
    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status)  {
        Long chatRoomId = getChatRoomId(session);
        Long userId= getUserId(session);
        rooms.get(chatRoomId).remove(session);
        log.info("{} 채팅방에서 나갔습니다.", chatRoomId);
    }

    private Long getChatRoomId(WebSocketSession session) {
        return Long.parseLong(session.getAttributes().get("chatRoomId").toString());
    }
    private Long getUserId(WebSocketSession session) {

        //테스트를 위해 임시적으로 변환
        Object userId = session.getAttributes().get("userId");
        return userId != null ? Long.parseLong(userId.toString()) : 1L;
    }
}
