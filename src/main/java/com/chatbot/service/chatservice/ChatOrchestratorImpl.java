package com.chatbot.service.chatservice;

import com.chatbot.ai.state.ChatState;
import com.chatbot.model.chatbot.ChatRequest;
import com.chatbot.model.chatbot.ChatResponse;
import com.chatbot.util.ChatOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatOrchestratorImpl implements ChatOrchestrator {

    private final CompiledGraph<ChatState> compiledChatGraph;

    @Override
    public ChatResponse process(ChatRequest request, String username) {

        try {

            Map<String, Object> input = new HashMap<>();

            input.put(ChatState.CONVERSATION_ID, request.getChatId());
            input.put(ChatState.USERNAME, username);
            input.put(ChatState.USER_MESSAGE, request.getMessage());
            input.put(ChatState.MODEL, "llama3.2:3b");
            input.put(ChatState.SUCCESS, true);

            Optional<ChatState> result = compiledChatGraph.invoke(input);

            ChatState state = result.orElseThrow(
                    () -> new RuntimeException("Graph returned no state")
            );

            //ChatState state = (ChatState) result.get();

            if (!state.isSuccess()) {
                throw new RuntimeException(state.getError());
            }

            return ChatResponse.builder()
                    .conversationId(state.getConversationId())
                    .userMessage(state.getUserMessage())
                    .assistantMessage(state.getAssistantMessage())
                    .promptTokens(state.getPromptTokens())
                    .completionTokens(state.getCompletionTokens())
                    .totalTokens(state.getTotalTokens())
                    .build();

        } catch (Exception ex) {

            log.error("Graph execution failed", ex);
            throw new RuntimeException("Graph execution failed", ex);
        }
    }
}