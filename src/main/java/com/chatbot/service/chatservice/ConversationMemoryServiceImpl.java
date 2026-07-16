package com.chatbot.service.chatservice;

import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.chatbot.ConversationMemory;
import com.chatbot.model.dto.response.MessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversationMemoryServiceImpl
        implements ConversationMemoryService {

    private final MessageService messageService;

    @Override
    public ConversationMemory loadMemory(UUID conversationId, String username) {

        try {
            List<MessageResponse> responses = messageService.getRecentMessages(conversationId, username);
            // oldest -> newest
            Collections.reverse(responses);
            List<String> history = responses.stream()
                    .map(message ->
                            message.getRole().name() + ": " + message.getContent())
                    .toList();
            log.info("ConversationId = {}", conversationId);
            log.info("Loaded {} messages", responses.size());

            responses.forEach(m ->
                    log.info("{} : {}", m.getRole(), m.getContent()));

            return new ConversationMemory(history);
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}