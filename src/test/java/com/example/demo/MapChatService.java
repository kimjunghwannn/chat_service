package com.example.demo;

import com.example.demo.domain.global.ChatHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@ExtendWith(MockitoExtension.class)
class MapChatService {

    @Mock
    private WebSocketSession session;
    @InjectMocks
    private ChatHandler chatHandler;


    @Test
    void concurrentRoomValueTest() throws InterruptedException {
        ConcurrentHashMap<Long, Set<Integer>> rooms = new ConcurrentHashMap<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        AtomicInteger counter = new AtomicInteger();
        Long chatRoomId = 1L;
        Runnable task = () -> {
            int value = counter.getAndIncrement();
            Long key = (long)counter.getAndIncrement();
            rooms.computeIfAbsent(chatRoomId, k -> ConcurrentHashMap.newKeySet()).add(value);
            rooms.computeIfAbsent(key, k -> ConcurrentHashMap.newKeySet()).add(value);
        };

        for (int i = 0; i < 1000; i++) {
            executor.submit(task);
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("총 세션 수: " + rooms.get(chatRoomId).size());
        System.out.println("총 키의 개수: " + rooms.keySet().size());
    }
}
