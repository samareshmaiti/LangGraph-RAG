package com.chatbot.ai.prompt;
import com.chatbot.ai.state.ChatState;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class GeneralPrompt implements BasePrompt {

    @Override
    public String build(ChatState state) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are ChatBot, a professional AI assistant.

                Instructions:

                1. Answer the user's question accurately.
                2. Maintain conversation context.
                3. Use Tool Results when available.
                4. Use Knowledge Base if relevant.
                5. Otherwise use general knowledge.
                6. Never fabricate information.
                7. If uncertain, clearly state it.
                8. Keep responses concise.
                9. Use Markdown when appropriate.

                Current Date:
                """);

        prompt.append(LocalDate.now());

        if (!state.getChatHistory().isEmpty()) {

            prompt.append("""

                    Conversation History:
                    """);

            state.getChatHistory()
                    .forEach(msg -> prompt.append(msg).append("\n"));
        }

        if (!state.getToolResults().isEmpty()) {

            prompt.append("""

                    Tool Results:
                    """);

            state.getToolResults()
                    .forEach(result -> prompt.append(result).append("\n"));
        }

        if (!state.getRetrievedContext().isEmpty()) {

            prompt.append("""

                    Knowledge Base:
                    """);

            state.getRetrievedContext()
                    .forEach(chunk -> prompt.append("- ").append(chunk).append("\n"));
        }

        prompt.append("""

                User Question:
                """);

        prompt.append(state.getUserMessage());

        return prompt.toString();
    }
}