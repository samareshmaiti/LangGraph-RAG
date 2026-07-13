package com.chatbot.mapper;


import com.chatbot.model.chatbot.Conversations;
import com.chatbot.model.dto.response.ConversationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConversationMapper {

    private final MessageMapper messageMapper;

    public ConversationResponse toResponse(Conversations conversation) {

        if (conversation == null) {
            return null;
        }

        return ConversationResponse.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .username(conversation.getUsername())
                .active(conversation.getActive())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .totalMessages(
                        conversation.getMessages() == null
                                ? 0
                                : conversation.getMessages().size()
                )
                .messages(
                        conversation.getMessages() == null
                                ? Collections.emptyList()
                                : conversation.getMessages()
                                .stream()
                                .map(messageMapper::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}