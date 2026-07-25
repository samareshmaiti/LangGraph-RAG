package com.chatbot.ai.node.tools;

import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GuardrailNode implements NodeAction<ChatState> {

    // Simple list of inappropriate keywords (in a real app, use a proper moderation model)
    private static final String[] INAPPROPRIATE_KEYWORDS = {
            "hate", "violence", "harassment", "illegal", "adult", "weapon", "drug"
    };

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Checking if guardrail tool is needed");

        String userMessage = state.getUserMessage().toLowerCase();
        boolean containsInappropriate = false;
        for (String keyword : INAPPROPRIATE_KEYWORDS) {
            if (userMessage.contains(keyword)) {
                containsInappropriate = true;
                break;
            }
        }

        // Always run guardrail to check content, but we only add a message if needed
        List<String> currentResults = state.getToolResults();
        List<String> updatedResults = new ArrayList<>();
        if (currentResults != null) {
            updatedResults.addAll(currentResults);
        }

        if (containsInappropriate) {
            String warning = "Warning: The request contains potentially inappropriate content. I cannot assist with that request.";
            updatedResults.add(warning);
            log.warn("Inappropriate content detected");
            // Also set an error state? We could set SUCCESS to false and ERROR message
            // For now, we just add the warning and let LLM handle it
            return Map.of(
                    ChatState.TOOL_RESULTS, updatedResults
            );
        } else {
            // Optionally add a note that content is safe
            // currentResults.add("Content passes safety checks.");
            log.info("Content passes safety checks");
            return Map.of(); // No change to state
        }
    }
}