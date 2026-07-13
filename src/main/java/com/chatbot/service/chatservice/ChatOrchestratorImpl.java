package com.chatbot.service.chatservice;

import com.chatbot.ai.node.LlmNode;
import com.chatbot.ai.node.PromptNode;
import com.chatbot.ai.node.ResponseNode;
import com.chatbot.ai.state.ChatState;
import com.chatbot.model.chatbot.ChatRequest;
import com.chatbot.model.chatbot.ChatResponse;
import com.chatbot.util.ChatOrchestrator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatOrchestratorImpl implements ChatOrchestrator {

    private final PromptNode promptNode;
    private final LlmNode llmNode;
    private final ResponseNode responseNode;

    @Override
    public ChatResponse process(ChatRequest request,
                                String username) {

        log.info("Executing AI pipeline...");

        Map<String, Object> stateData = new HashMap<>();

        stateData.put(ChatState.CONVERSATION_ID, request.getChatId());
        stateData.put(ChatState.USERNAME, username);
        stateData.put(ChatState.USER_MESSAGE, request.getMessage());
        stateData.put(ChatState.MODEL, "llama3.2");
        stateData.put(ChatState.SUCCESS, true);

        ChatState state = new ChatState(stateData);

        /*
         * Prompt Node
         */
        Map<String, Object> promptUpdate = promptNode.execute(state);
        stateData.putAll(promptUpdate);
        state = new ChatState(stateData);

        /*
         * LLM Node
         */
        Map<String, Object> llmUpdate = llmNode.execute(state);
        stateData.putAll(llmUpdate);
        state = new ChatState(stateData);

        /*
         * Response Node
         */
        Map<String, Object> responseUpdate = responseNode.execute(state);
        stateData.putAll(responseUpdate);
        state = new ChatState(stateData);

        return ChatResponse.builder()
                .conversationId(state.getConversationId())
                .userMessage(state.getUserMessage())
                .assistantMessage(state.getAssistantMessage())
                .promptTokens(0)
                .completionTokens(0)
                .totalTokens(0)
                .build();
    }
}