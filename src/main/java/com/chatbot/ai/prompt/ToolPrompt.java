package com.chatbot.ai.prompt;

import com.chatbot.ai.state.ChatState;
import org.springframework.stereotype.Component;

@Component
public class ToolPrompt implements BasePrompt {

    @Override
    public String build(ChatState state) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are the Tool Agent.

                Instructions:

                1. Use Tool Results as the source of truth.
                2. Never contradict tool results.
                3. Do not guess if the tool returns no data.
                4. Explain results clearly.

                Tool Results:
                """);

        state.getToolResults()
                .forEach(result -> prompt.append(result).append("\n"));

        prompt.append("""

                User Question:
                """);

        prompt.append(state.getUserMessage());

        return prompt.toString();
    }
}
