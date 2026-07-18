package com.chatbot.ai.prompt;
import com.chatbot.ai.state.ChatState;
import org.springframework.stereotype.Component;

@Component
public class MemoryPrompt implements BasePrompt {

    @Override
    public String build(ChatState state) {

        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are the Memory Agent.

                Instructions:

                1. Answer ONLY using Conversation History.
                2. Ignore Knowledge Base.
                3. Ignore general knowledge.
                4. Never guess.
                5. If the answer is missing, say you don't know.

                Conversation History:
                """);

        state.getChatHistory()
                .forEach(msg -> prompt.append(msg).append("\n"));

        prompt.append("""

                User Question:
                """);

        prompt.append(state.getUserMessage());

        return prompt.toString();
    }
}
