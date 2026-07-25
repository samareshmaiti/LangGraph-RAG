package com.chatbot.ai.node;

import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class RouterNode implements NodeAction<ChatState> {

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Routing request to determine relevant tools");

        String userMessage = state.getUserMessage().toLowerCase();
        List<String> relevantTools = new ArrayList<>();

        // Define tool keywords
        String[] weatherKeywords = {"weather", "temperature", "forecast", "rain", "snow", "sunny", "cloudy", "humidity", "climate"};
        String[] codingKeywords = {"code", "program", "function", "class", "debug", "bug", "algorithm", "syntax", "java", "python", "javascript", "html", "css", "script", "develop"};
        String[] planningKeywords = {"plan", "steps", "how to", "strategy", "approach", "process", "roadmap", "organize"};
        String[] summarizerKeywords = {"summarize", "summary", "tl;dr", "brief", "shorten", "abstract", "recap", "summarise"};
        String[] guardrailKeywords = {"hate", "violence", "harassment", "illegal", "adult", "weapon", "drug"};

        // Check each category
        if (containsAny(userMessage, weatherKeywords)) {
            relevantTools.add("weather");
            log.debug("Routing to weather tool");
        }

        if (containsAny(userMessage, codingKeywords)) {
            relevantTools.add("coding");
            log.debug("Routing to coding tool");
        }

        if (containsAny(userMessage, planningKeywords)) {
            relevantTools.add("planner");
            log.debug("Routing to planner tool");
        }

        if (containsAny(userMessage, summarizerKeywords)) {
            relevantTools.add("summarizer");
            log.debug("Routing to summarizer tool");
        }

        // Always run guardrail for safety checking
        relevantTools.add("guardrail");
        log.debug("Always routing to guardrail tool for safety");

        // Remove duplicates
        List<String> distinctTools = new ArrayList<>(new java.util.HashSet<>(relevantTools));

        log.info("Relevant tools determined: {}", distinctTools);

        // Store the result in the returned map to be merged with state
        Map<String, Object> routerResult = new HashMap<>();
        routerResult.put("relevantTools", distinctTools);

        Map<String, Object> result = new HashMap<>();
        result.put(ChatState.ROUTER_RESULT, routerResult);
        result.put(ChatState.SUCCESS, true);

        return result;
    }

    private boolean containsAny(String text, String[] keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}