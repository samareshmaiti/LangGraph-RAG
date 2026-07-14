package com.chatbot.service.chatservice;

import com.chatbot.ai.state.ChatState;
import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.chatbot.ChatRequest;
import com.chatbot.model.chatbot.ChatResponse;
import com.chatbot.model.dto.request.CreateConversationRequest;
import com.chatbot.model.dto.request.MessageRequest;

import org.bsc.langgraph4j.CompiledGraph;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        log.info("Processing chat request. username={}, conversationId={}", username, request.getChatId());

         // STEP 1:Create conversation if first message

        UUID conversationId = request.getChatId();
        if (conversationId == null) {
            conversationId =
                    conversationService.createConversation(
                            CreateConversationRequest.builder()
                                    .title(request.getMessage())
                                    .build(),
                            username
                    );
            request.setChatId(conversationId);
            log.info("New conversation created {}", conversationId);
        }



        /*
         * STEP 2:
         * Save user message
         */
        messageService.saveUserMessage(
                MessageRequest.builder()
                        .conversationId(conversationId)
                        .content(request.getMessage())
                        .build(),
                username
        );
        // Create LangGraph state

        ChatState state = new ChatState();
        state.setConversationId(conversationId);
        state.setUsername(username);
        state.setUserMessage(request.getMessage());

         // STEP 4: Execute LangGraph

        ChatState finalState;
        try {
            Map<String, Object> input = new HashMap<>();
            input.put(ChatState.CONVERSATION_ID, conversationId);
            input.put(ChatState.USERNAME, username);
            input.put(ChatState.USER_MESSAGE, request.getMessage());

            Optional<ChatState> result = compiledChatGraph.invoke(input).map(ChatState.class::cast);
            finalState = result.orElseThrow(() -> new RuntimeException("Graph returned empty state"));

        } catch (Exception e) {
            log.error("AI graph execution failed", e);
            throw new RuntimeException(e);
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

         // Return API response

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