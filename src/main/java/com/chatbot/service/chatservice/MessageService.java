package com.chatbot.service.chatservice;


import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.dto.request.MessageRequest;
import com.chatbot.model.dto.response.MessageResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    /**
     * Save a USER message.
     */
    MessageResponse saveUserMessage(MessageRequest request, String username) throws ResourceNotFoundException;
    /**
     * Save an ASSISTANT message.
     */
    MessageResponse saveAssistantMessage(UUID conversationId, String content, Integer promptTokens, Integer completionTokens, Integer totalTokens, String username) throws ResourceNotFoundException;

    /**
     * Get conversation history.
     */
    List<MessageResponse> getConversationMessages(UUID conversationId, String username) throws ResourceNotFoundException;

    /**
     * Delete a message.
     */
    void deleteMessage(UUID messageId, String username) throws ResourceNotFoundException;

    @Transactional(readOnly = true)
    List<String> getConversationHistory(UUID conversationId, String username)
            throws ResourceNotFoundException;
    List<MessageResponse> getRecentMessages(
            UUID conversationId,
            String username)
            throws ResourceNotFoundException;
}