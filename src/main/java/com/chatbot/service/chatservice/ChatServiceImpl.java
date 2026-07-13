package com.chatbot.service.chatservice;

import com.chatbot.model.chatbot.ChatRequest;
import com.chatbot.model.chatbot.ChatResponse;
import com.chatbot.model.dto.request.MessageRequest;
import com.chatbot.model.dto.response.MessageResponse;
import com.chatbot.util.ChatOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class ChatServiceImpl implements ChatService {
    @Autowired
    private  MessageService messageService;
    @Autowired
    private  ChatOrchestrator chatOrchestrator;

    @Override
    public ChatResponse chat(ChatRequest request, String username) {

        log.info("Processing chat request for user {}", username);

        /*
         * STEP-1
         * Save USER message
         */
        MessageResponse userMessage = messageService.saveUserMessage(
                MessageRequest.builder()
                        .conversationId(request.getChatId())
                        .content(request.getMessage())
                        .build(),
                username
        );

        /*
         * STEP-2
         * Generate AI response
         */
        ChatResponse aiResponse = chatOrchestrator.process(request, username);

        /*
         * STEP-3
         * Save ASSISTANT response
         */
        messageService.saveAssistantMessage(
                request.getChatId(),
                aiResponse.getAssistantMessage(),
                aiResponse.getPromptTokens(),
                aiResponse.getCompletionTokens(),
                aiResponse.getTotalTokens(),
                username
        );

        /*
         * STEP-4
         * Return response
         */
        return aiResponse;
    }
}