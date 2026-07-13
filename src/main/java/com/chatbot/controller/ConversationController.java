package com.chatbot.controller;


import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.dto.request.CreateConversationRequest;
import com.chatbot.model.dto.response.ConversationResponse;
import com.chatbot.service.chatservice.ConversationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping
    public ResponseEntity<ConversationResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request,
            Authentication authentication) {

        ConversationResponse response = conversationService.createConversation(
                request,
                authentication.getName());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ConversationResponse>> getConversations(
            Authentication authentication) {

        return ResponseEntity.ok(
                conversationService.getUserConversations(authentication.getName()));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationResponse> getConversation(
            @PathVariable UUID conversationId,
            Authentication authentication) {

        try {
            return ResponseEntity.ok(
                    conversationService.getConversation(
                            conversationId,
                            authentication.getName()));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{conversationId}")
    public ResponseEntity<ConversationResponse> updateConversation(
            @PathVariable UUID conversationId,
            @Valid @RequestBody CreateConversationRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                conversationService.updateConversation(
                        conversationId,
                        request,
                        authentication.getName()));
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(
            @PathVariable UUID conversationId,
            Authentication authentication) {

        conversationService.deleteConversation(
                conversationId,
                authentication.getName());

        return ResponseEntity.noContent().build();
    }
}