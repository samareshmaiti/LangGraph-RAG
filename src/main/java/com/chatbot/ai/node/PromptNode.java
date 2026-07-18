package com.chatbot.ai.node;

import com.chatbot.ai.prompt.PromptFactory;
import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PromptNode implements NodeAction<ChatState> {

    private final PromptFactory promptFactory;

    @Override
    public Map<String, Object> apply(ChatState state) {

        String agent = state.getCurrentAgent();

        log.info("Building prompt for agent: {}", agent);

        String prompt = promptFactory
                .getPrompt(agent)
                .build(state);

        log.debug("Prompt:\n{}", prompt);

        return Map.of(
                ChatState.PROMPT, prompt,
                ChatState.SUCCESS, true
        );
    }
}