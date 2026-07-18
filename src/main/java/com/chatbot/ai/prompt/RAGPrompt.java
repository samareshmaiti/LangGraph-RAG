package com.chatbot.ai.prompt;
import com.chatbot.ai.state.ChatState;
import org.springframework.stereotype.Component;

@Component
public class RAGPrompt implements BasePrompt {

    @Override
    public String build(ChatState state) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are the Knowledge Base Agent.

                Instructions:

                1. Use Retrieved Knowledge as the primary source.
                2. Use Conversation History only for context.
                3. Do not invent information.
                4. If the documents do not contain the answer, clearly say so.
                5. Keep responses concise.

                Retrieved Knowledge:
                """);

        state.getRetrievedContext()
                .forEach(chunk -> prompt.append("- ").append(chunk).append("\n"));

        if (!state.getChatHistory().isEmpty()) {

            prompt.append("""

                    Conversation History:
                    """);

            state.getChatHistory()
                    .forEach(msg -> prompt.append(msg).append("\n"));
        }

        prompt.append("""

                User Question:
                """);

        prompt.append(state.getUserMessage());

        return prompt.toString();
    }
}
