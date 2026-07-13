package com.chatbot.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateConversationRequest {

    @NotBlank(message = "Conversation title is required")
    @Size(max = 100, message = "Title cannot exceed 100 characters")
    private String title;
}