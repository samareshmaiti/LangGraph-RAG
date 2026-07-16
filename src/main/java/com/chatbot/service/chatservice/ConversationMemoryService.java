package com.chatbot.service.chatservice;

import com.chatbot.model.chatbot.ConversationMemory;

import java.util.UUID;

public interface ConversationMemoryService {

    ConversationMemory loadMemory(
            UUID conversationId,
            String username);

}