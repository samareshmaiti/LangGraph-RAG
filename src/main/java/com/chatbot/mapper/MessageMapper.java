package com.chatbot.mapper;


import com.chatbot.model.chatbot.Message;
import com.chatbot.model.dto.response.MessageResponse;
import com.chatbot.util.MessageRole;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public MessageResponse toResponse(Message message) {

        if (message == null) {
            return null;
        }

        return MessageResponse.builder()
                .id(message.getId())
                .conversationId(message.getConversation().getId())
                .role(message.getRole())
                .content(message.getContent())
                .promptTokens(message.getPromptTokens())
                .completionTokens(message.getCompletionTokens())
                .totalTokens(message.getTotalTokens())
                .createdAt(message.getCreatedAt())
                .build();
    }

    /**
     * Creates a USER message.
     */
    public Message toUserMessage(String content) {

        return Message.builder()
                .content(content)
                .role(MessageRole.USER)
                .build();
    }

    /**
     * Creates an ASSISTANT message.
     */
    public Message toAssistantMessage(
            String content,
            Integer promptTokens,
            Integer completionTokens,
            Integer totalTokens) {

        return Message.builder()
                .content(content)
                .role(MessageRole.ASSISTANT)
                .promptTokens(promptTokens)
                .completionTokens(completionTokens)
                .totalTokens(totalTokens)
                .build();
    }

    /**
     * Creates a SYSTEM message.
     */
    public Message toSystemMessage(String content) {

        return Message.builder()
                .content(content)
                .role(MessageRole.SYSTEM)
                .build();
    }
}