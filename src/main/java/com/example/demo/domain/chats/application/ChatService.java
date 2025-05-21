package com.example.demo.domain.chats.application;

import com.example.demo.domain.chats.dto.ChatMessageDto;
import com.example.demo.domain.chats.dto.CreateChatDto;
import com.example.demo.domain.chats.entity.ChatRoom;
import com.example.demo.domain.chats.entity.ChatRoomRepository;
import com.example.demo.domain.global.ChatHandler;
import com.example.demo.domain.user.application.UserService;
import com.example.demo.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final ChatHandler chatHandler;
    private final RedisTemplate<String,String> redisTemplate;
    @Transactional
    public void sendChatMessage(ChatMessageDto chatMessageDto)
    {
        try
        {
            Long chatRoomId=chatMessageDto.chatRoomId();
            long nowEpoch = Instant.now().getEpochSecond();
            String key = "chat:" + chatRoomId + ":msg_count:" + nowEpoch;
            boolean isHotChat=false;
            Long count = redisTemplate.opsForValue().increment(key);

            if (count == 1) {

                redisTemplate.expire(key, Duration.ofSeconds(1));
            }
            if(count>=5)
                isHotChat=true;
            String message = chatMessageDto.senderName()+": "+ chatMessageDto.payload();
            chatHandler.handleTextMessage(chatRoomId, message,isHotChat);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void createChatRoom(CreateChatDto createChatDto)
    {
        Users user = userService.findById(createChatDto.userId());
        ChatRoom chatRoom = ChatRoom.builder().build();

        chatRoom.getMembers().add(user);
        user.setChatRoom(chatRoom);

        chatRoomRepository.save(chatRoom);
    }
}
