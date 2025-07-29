package com.example.demo.domain.mapping.presentation;

import com.example.demo.domain.mapping.application.MappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mapping")
@RequiredArgsConstructor
public class MappingController {

    private final MappingService mappingService;

    @PostMapping
    public ResponseEntity<String> mapChatRoom(@RequestBody Long chatRoomId)
    {

        return ResponseEntity.ok(mappingService.mapping(chatRoomId));
    }
}