package com.chatbot.model.chatbot;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ChatRequest {

    @NotNull(message = "chat id must be not null")
    private UUID chatId;
    @NotNull(message = "message should be not null")
    private String message;
}
