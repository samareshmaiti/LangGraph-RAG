package com.chatbot.service.chatservice;

import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.chatbot.ChatRequest;
import com.chatbot.model.chatbot.ChatResponse;

public interface ChatService {
    ChatResponse chat(ChatRequest request,String username) throws ResourceNotFoundException;
}
