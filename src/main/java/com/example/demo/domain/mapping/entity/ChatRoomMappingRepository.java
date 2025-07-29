package com.example.demo.domain.mapping.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface ChatRoomMappingRepository extends JpaRepository<ChatRoomMapping, Long>
{
    @Query("SELECT m.serverId AS serverId, COUNT(m) AS load " +
            "FROM ChatRoomMapping m GROUP BY m.serverId")
    List<ServerLoadProjection> countChatRoomsByServer();
}

