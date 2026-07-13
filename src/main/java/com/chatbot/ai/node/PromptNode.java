package com.chatbot.ai.node;

import com.chatbot.ai.state.ChatState;
import com.chatbot.ai.state.PromptTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromptNode {

    private final PromptTemplate promptTemplate;

    public Map<String, Object> execute(ChatState state) {

        log.info("Building prompt for conversation: {}", state.getConversationId());

        String prompt = promptTemplate.build(state);

        log.debug("Generated Prompt:\n{}", prompt);

        return Map.of(
                ChatState.PROMPT, prompt,
                ChatState.SUCCESS, true
        );
    }
}