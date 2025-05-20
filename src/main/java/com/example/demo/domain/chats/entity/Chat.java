package com.example.demo.domain.chats.entity;

import com.example.demo.domain.user.entity.Users;
import jakarta.persistence.*;

@Entity
public class Chat
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String payload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Users sender;
}
