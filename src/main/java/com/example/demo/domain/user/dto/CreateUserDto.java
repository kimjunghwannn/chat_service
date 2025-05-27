package com.example.demo.domain.user.dto;

import com.example.demo.domain.user.entity.Users;

public record CreateUserDto(
        String userName
) {
    public Users toUser() {
        return Users.builder()
                .username(userName)
                .build();
    }
}
