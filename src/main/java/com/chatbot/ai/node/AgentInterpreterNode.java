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
public class AgentInterpreterNode implements NodeAction<ChatState> {

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Interpreting agent classification result");

        // Check if this is a classification result we need to interpret
        if (!Boolean.TRUE.equals(state.getIsClassificationTask())) {
            // Not a classification task, just pass through
            return Map.of();
        }

        // Get the LLM's response which should be the agent type
        String agentResponse = state.getAssistantMessage();
        if (agentResponse == null) {
            agentResponse = "";
        }
        agentResponse = agentResponse.trim().toUpperCase();

        log.info("Raw classification result from LLM: '{}'", agentResponse);

        // Validate and clean up the response
        String selectedAgent = "GENERAL"; // Default fallback
        switch (agentResponse) {
            case "MEMORY":
            case "RAG":
            case "TOOL":
                selectedAgent = agentResponse;
                break;
            default:
                // Try to extract a valid agent type from the response
                if (agentResponse.contains("MEMORY")) {
                    selectedAgent = "MEMORY";
                } else if (agentResponse.contains("RAG")) {
                    selectedAgent = "RAG";
                } else if (agentResponse.contains("TOOL")) {
                    selectedAgent = "TOOL";
                } else {
                    log.warn("Invalid or unrecognized agent type from LLM: '{}'. Defaulting to GENERAL.", agentResponse);
                }
                break;
        }

        // Restore the original user message if we saved it
        String originalUserMessage = state.getOriginalUserMessage();
        if (originalUserMessage != null) {
            state.setUserMessage(originalUserMessage);
        }

        // Clear classification flags
        state.setIsClassificationTask(false);
        state.setOriginalUserMessage(null);

        // Set the determined agent type
        state.setCurrentAgent(selectedAgent);
        log.info("Final agent selection: {}", selectedAgent);

        return Map.of(
                "agentSelected", selectedAgent
        );
    }
}
