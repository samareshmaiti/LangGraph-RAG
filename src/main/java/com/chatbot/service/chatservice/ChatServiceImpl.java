package com.chatbot.service.chatservice;

import com.chatbot.ai.state.ChatState;
import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.chatbot.ChatRequest;
import com.chatbot.model.chatbot.ChatResponse;
import com.chatbot.model.dto.request.CreateConversationRequest;
import com.chatbot.model.dto.request.MessageRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatServiceImpl implements ChatService {

    private final MessageService messageService;
    private final ConversationService conversationService;
    private final CompiledGraph<ChatState> compiledChatGraph;

    @Override
    public ChatResponse chat(ChatRequest request, String username) throws ResourceNotFoundException {

        log.info("Processing chat request. username={}, conversationId={}",
                username, request.getChatId());

        UUID conversationId = request.getChatId();

        // Create conversation for first message
        if (conversationId == null) {

            conversationId = conversationService.createConversation(
                    CreateConversationRequest.builder()
                            .title(request.getMessage())
                            .build(),
                    username
            );

            request.setChatId(conversationId);

            log.info("Created new conversation {}", conversationId);
        }

        // Save current user message
        messageService.saveUserMessage(
                MessageRequest.builder()
                        .conversationId(conversationId)
                        .content(request.getMessage())
                        .build(),
                username
        );

        // Load complete conversation history
        List<String> chatHistory =
                messageService.getConversationHistory(conversationId, username);

        // Build graph input
        Map<String, Object> input = new HashMap<>();
        input.put(ChatState.CONVERSATION_ID, conversationId);
        input.put(ChatState.USERNAME, username);
        input.put(ChatState.USER_MESSAGE, request.getMessage());
        input.put(ChatState.CHAT_HISTORY, chatHistory);

        ChatState finalState;

        try {

            finalState = compiledChatGraph
                    .invoke(input)
                    .map(ChatState.class::cast)
                    .orElseThrow(() ->
                            new RuntimeException("Graph returned empty state"));

        } catch (Exception ex) {

            log.error("Failed to execute LangGraph", ex);
            throw new RuntimeException("AI processing failed", ex);
        }

        // Save assistant response
        messageService.saveAssistantMessage(
                conversationId,
                finalState.getAssistantMessage(),
                finalState.getPromptTokens(),
                finalState.getCompletionTokens(),
                finalState.getTotalTokens(),
                username
        );

        return ChatResponse.builder()
                .conversationId(conversationId)
                .userMessage(request.getMessage())
                .assistantMessage(finalState.getAssistantMessage())
                .promptTokens(finalState.getPromptTokens())
                .completionTokens(finalState.getCompletionTokens())
                .totalTokens(finalState.getTotalTokens())
                .createdAt(LocalDateTime.now())
                .build();
    }
}