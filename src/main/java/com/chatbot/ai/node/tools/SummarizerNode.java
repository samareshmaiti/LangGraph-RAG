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
public class SummarizerNode implements NodeAction<ChatState> {

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Checking if summarizer tool is needed");

        String userMessage = state.getUserMessage().toLowerCase();
        // Check for summarization-related keywords
        boolean isSummarizationQuery = userMessage.contains("summarize") ||
                userMessage.contains("summary") ||
                userMessage.contains("tl;dr") ||
                userMessage.contains("brief") ||
                userMessage.contains("shorten") ||
                userMessage.contains("abstract") ||
                userMessage.contains("recap");

        if (!isSummarizationQuery) {
            log.info("Summarizer tool not needed");
            return Map.of();
        }

        log.info("Executing summarizer tool");

        // Get text to summarize: prefer user message, but could also summarize conversation
        String textToSummarize = state.getUserMessage();
        if (state.getChatHistory() != null && !state.getChatHistory().isEmpty()) {
            // Combine recent conversation for summarization
            StringBuilder sb = new StringBuilder();
            int start = Math.max(0, state.getChatHistory().size() - 5); // last 5 messages
            for (int i = start; i < state.getChatHistory().size(); i++) {
                sb.append(state.getChatHistory().get(i)).append(" ");
            }
            textToSummarize = sb.toString().trim();
            if (textToSummarize.isEmpty()) {
                textToSummarize = state.getUserMessage();
            }
        }

        // Simple summarization: extract first few sentences or limit length
        String summary = summarizeText(textToSummarize);

        // Append to existing tool results
        List<String> currentResults = state.getToolResults();
        List<String> updatedResults = new ArrayList<>();
        if (currentResults != null) {
            updatedResults.addAll(currentResults);
        }
        updatedResults.add("Summary: " + summary);

        return Map.of(
                ChatState.TOOL_RESULTS, updatedResults
        );
    }

    private String summarizeText(String text) {
        if (text == null || text.isEmpty()) {
            return "No content to summarize";
        }
        // Simple approach: take first 200 characters
        if (text.length() > 200) {
            return text.substring(0, 200) + "...";
        }
        return text;
    }
}