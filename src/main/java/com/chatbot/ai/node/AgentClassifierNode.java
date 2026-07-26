package com.chatbot.ai.node;


import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AgentClassifierNode implements NodeAction<ChatState> {

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Preparing for LLM-based agent classification");

        // Save the original user message so we can restore it after classification
        String originalUserMessage = state.getUserMessage();
        if (originalUserMessage != null) {
            state.setOriginalUserMessage(originalUserMessage);
        }

        // Mark this as a classification task so the PromptNode knows to use the classification prompt
        state.setIsClassificationTask(true);

        log.info("Prepared classification task. Original message saved: {}", 
                originalUserMessage != null ? originalUserMessage.substring(0, Math.min(50, originalUserMessage.length())) + "..." : "null");

        return Map.of(
                "classificationPrepared", true
        );
    }
}
