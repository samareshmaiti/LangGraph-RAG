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
public class CodingNode implements NodeAction<ChatState> {

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Checking if coding tool is needed");

        String userMessage = state.getUserMessage().toLowerCase();
        // Check for coding-related keywords
        boolean isCodingQuery = userMessage.contains("code") ||
                userMessage.contains("program") ||
                userMessage.contains("function") ||
                userMessage.contains("class") ||
                userMessage.contains("debug") ||
                userMessage.contains("bug") ||
                userMessage.contains("algorithm") ||
                userMessage.contains("syntax") ||
                userMessage.contains("java") ||
                userMessage.contains("python") ||
                userMessage.contains("javascript") ||
                userMessage.contains("html") ||
                userMessage.contains("css");

        if (!isCodingQuery) {
            log.info("Coding tool not needed");
            return Map.of();
        }

        log.info("Executing coding tool with user message {}",userMessage);

        // Provide coding assistance - in a real app, this could use a code generation model
        String codingHelp = "I can help you with coding questions. Please provide more details about what you need help with (e.g., language, specific problem, code snippet).";

        // Append to existing tool results
        List<String> currentResults = state.getToolResults();
        List<String> updatedResults = new ArrayList<>();
        if (currentResults != null) {
            updatedResults.addAll(currentResults);
        }
        updatedResults.add(codingHelp);

        return Map.of(
                ChatState.TOOL_RESULTS, updatedResults
        );
    }
}