package com.example.demo.domain.mapping.application;

import com.example.demo.domain.mapping.entity.ChatRoomMapping;
import com.example.demo.domain.mapping.entity.ChatRoomMappingRepository;
import com.example.demo.domain.mapping.entity.ServerLoadProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MappingService
{
    private final List<String> serverIds = List.of("demo1", "demo2", "demo3");
    private final ChatRoomMappingRepository mappingRepository;

    @Transactional
    public String mapping(Long chatRoomId)
    {
        List<ServerLoadProjection> loadList = mappingRepository.countChatRoomsByServer();

        for(ServerLoadProjection load : loadList)
            System.out.println(load);
        Map<String, Long> loadMap = loadList.stream()
                .collect(Collectors.toMap(ServerLoadProjection::getServerId, ServerLoadProjection::getLoad));

        String selectedServer = serverIds.stream()
                .min(Comparator.comparing(serverId -> loadMap.getOrDefault(serverId, 0L)))
                .orElse("demo1");

        ChatRoomMapping mapping = ChatRoomMapping.of(chatRoomId, selectedServer);
        log.info("Mapping to save: {}", mapping.toString());
        mappingRepository.save(mapping);

        return selectedServer;
    }
}
