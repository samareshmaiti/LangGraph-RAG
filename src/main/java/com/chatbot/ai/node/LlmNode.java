package com.chatbot.ai.node;

import com.chatbot.ai.provider.AiProvider;
import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class LlmNode {

    private final AiProvider aiProvider;

    public Map<String, Object> execute(ChatState state) {

        log.info("Executing LLM Node...");

        if (state.getPrompt() == null || state.getPrompt().isBlank()) {

            return Map.of(
                    ChatState.SUCCESS, false,
                    ChatState.ERROR, "Prompt is empty."
            );
        }

        try {

            String response = aiProvider.generate(state);

            return Map.of(
                    ChatState.ASSISTANT_MESSAGE, response,
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
}