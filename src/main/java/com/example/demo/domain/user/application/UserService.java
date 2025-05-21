package com.example.demo.domain.user.application;

import com.example.demo.domain.user.dto.CreateUserDto;
import com.example.demo.domain.user.entity.UserRepository;
import com.example.demo.domain.user.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService
{
    private final UserRepository userRepository;

    @Transactional
    public void create(CreateUserDto createUserDto)
    {
        userRepository.save(createUserDto.toUser());
    }

    public Users findById(Long id
    )
    {
        return userRepository.findById(id).orElse(null);
    }
}
