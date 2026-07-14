package com.chatbot.service.chatservice;



import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.dto.request.CreateConversationRequest;
import com.chatbot.model.dto.response.ConversationResponse;

import java.util.List;
import java.util.UUID;

public interface ConversationService {

//ConversationResponse createConversation(CreateConversationRequest request, String username);

    ConversationResponse getConversation(UUID conversationId, String username) throws ResourceNotFoundException;

    List<ConversationResponse> getUserConversations(String username);

    ConversationResponse updateConversation(UUID conversationId, CreateConversationRequest request, String username);

    void deleteConversation(UUID conversationId, String username);
    UUID createConversation(CreateConversationRequest request, String username);
}