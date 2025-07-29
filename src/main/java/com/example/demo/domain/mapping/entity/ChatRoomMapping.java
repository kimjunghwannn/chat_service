package com.example.demo.domain.mapping.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ChatRoomMapping
{
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String serverId;

    @Column(nullable = false)
    private Long chatRoomId;

    @Builder
    public static ChatRoomMapping of(Long chatRoomId, String  serverId) {
        return ChatRoomMapping.builder()
                .chatRoomId(chatRoomId)
                .serverId(serverId)

                .build();
    }

}
