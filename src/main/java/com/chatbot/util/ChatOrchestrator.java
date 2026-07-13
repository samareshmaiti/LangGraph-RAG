package com.chatbot.util;


import com.chatbot.model.chatbot.ChatRequest;
import com.chatbot.model.chatbot.ChatResponse;

public interface ChatOrchestrator {

    ChatResponse process(ChatRequest request, String username);

}