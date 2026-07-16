package com.chatbot.model.chatbot;

import com.chatbot.model.dto.response.MessageResponse;

import java.util.List;

public record ConversationMemory(

        List<String> messages

) {
}