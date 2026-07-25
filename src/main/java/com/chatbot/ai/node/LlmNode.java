package com.chatbot.ai.node;

import com.chatbot.ai.provider.AiProvider;
import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LlmNode implements NodeAction<ChatState> {

    private final AiProvider aiProvider;

    @Override
    public Map<String, Object> apply(ChatState state) {

        log.info("Executing LLM Node");

        if (state.getPrompt() == null || state.getPrompt().isBlank()) {
            return Map.of(
                    ChatState.SUCCESS, false,
                    ChatState.ERROR, "Prompt is empty."
            );
        }

        try {

            String response = aiProvider.generate(state);

            // Calculate token counts here
            int promptTokens = estimateTokens(state.getPrompt());
            int completionTokens = estimateTokens(response);

            return Map.of(
                    ChatState.ASSISTANT_MESSAGE, response,
                    ChatState.PROMPT_TOKENS, promptTokens,
                    ChatState.COMPLETION_TOKENS, completionTokens,
                    ChatState.TOTAL_TOKENS, promptTokens + completionTokens,
                    ChatState.SUCCESS, true
            );

        } catch (Exception ex) {

            log.error("LLM Error", ex);

            return Map.of(
                    ChatState.SUCCESS, false,
                    ChatState.ERROR, ex.getMessage()
            );
        }
    }

    private int estimateTokens(String text) {
        return text == null ? 0 : text.split("\\s+").length;
    }
}