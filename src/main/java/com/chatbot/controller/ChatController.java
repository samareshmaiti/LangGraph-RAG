package com.chatbot.controller;


import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.chatbot.ChatRequest;
import com.chatbot.model.chatbot.ChatResponse;
import com.chatbot.service.chatservice.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @Valid @RequestBody ChatRequest request,
            Authentication authentication) throws ResourceNotFoundException {

        ChatResponse response = chatService.chat(
                request,
                authentication.getName());

        return ResponseEntity.ok(response);
    }

}