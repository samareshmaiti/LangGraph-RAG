package com.chatbot.controller;


import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.dto.response.MessageResponse;
import com.chatbot.service.chatservice.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageResponse>> getConversationMessages(
            @PathVariable UUID conversationId,
            Authentication authentication) throws ResourceNotFoundException {

        return ResponseEntity.ok(
                messageService.getConversationMessages(
                        conversationId,
                        authentication.getName()));
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(
            @PathVariable UUID messageId,
            Authentication authentication) throws ResourceNotFoundException {

        messageService.deleteMessage(
                messageId,
                authentication.getName());

        return ResponseEntity.noContent().build();
    }
}