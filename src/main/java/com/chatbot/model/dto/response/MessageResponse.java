package com.chatbot.model.dto.response;


import com.chatbot.util.MessageRole;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;

    private UUID conversationId;

    private MessageRole role;

    private String content;

    private Integer promptTokens;

    private Integer completionTokens;

    private Integer totalTokens;

    private LocalDateTime createdAt;
}