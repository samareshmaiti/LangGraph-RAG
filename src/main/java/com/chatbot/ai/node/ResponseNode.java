package com.chatbot.ai.node;

import com.chatbot.ai.state.ChatState;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ResponseNode implements NodeAction<ChatState> {

    @Override
    public Map<String, Object> apply(ChatState state) {

        log.info("Formatting response");
        if (!Boolean.TRUE.equals(state.isSuccess())) {
            return Map.of(ChatState.SUCCESS, false, ChatState.ERROR, state.getError());
        }

        String response = state.getAssistantMessage();
        if (response == null || response.isBlank()) {
            return Map.of(ChatState.SUCCESS, false, ChatState.ERROR, "Empty response.");
        }

        response = response.trim();
        response = response.replaceAll("\\n{3,}", "\n\n");

        return Map.of(ChatState.ASSISTANT_MESSAGE, response, ChatState.SUCCESS, true);
    }
}