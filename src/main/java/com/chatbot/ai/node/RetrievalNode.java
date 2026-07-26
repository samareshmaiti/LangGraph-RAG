package com.chatbot.ai.node;


import com.chatbot.ai.state.ChatState;
import com.chatbot.service.rag.RetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RetrievalNode implements NodeAction<ChatState> {

    private final RetrievalService retrievalService;

    @Override
    public Map<String, Object> apply(ChatState state) {
        // Only perform retrieval for RAG agent type
        String currentAgent = state.getCurrentAgent();
        if (!"RAG".equalsIgnoreCase(currentAgent)) {
            log.info("Skipping retrieval for agent type: {}", currentAgent);
            // Return success with empty context to maintain flow
            return Map.of(
                    ChatState.RETRIEVED_CONTEXT, java.util.Collections.emptyList(),
                    ChatState.SUCCESS, true
            );
        }

        log.info("Executing Retrieval Node for RAG agent...");
        List<String> context = retrievalService.retrieve(state.getUserMessage());

        return Map.of(
                ChatState.RETRIEVED_CONTEXT, context,
                ChatState.SUCCESS, true
        );
    }
}