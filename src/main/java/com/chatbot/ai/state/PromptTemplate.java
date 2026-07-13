package com.chatbot.ai.state;

import com.chatbot.ai.state.ChatState;
import org.springframework.stereotype.Component;

@Component
public class PromptTemplate {

    private static final String SYSTEM_PROMPT = """
            You are ChatBot, a helpful, polite, and knowledgeable AI assistant.

            Rules:
            - Answer clearly and accurately.
            - If you don't know the answer, say so.
            - Keep responses concise unless the user asks for details.
            - Use Markdown formatting when appropriate.
            - Never fabricate information.
            """;

    public String build(ChatState state) {

        StringBuilder prompt = new StringBuilder();

        // System prompt
        prompt.append(SYSTEM_PROMPT);

        // Conversation history (future memory support)
        if (state.getConversationContext() != null
                && state.getConversationContext().length() > 0) {

            prompt.append("\n\nConversation History:\n");
            prompt.append(state.getConversationContext());
        }

        // Current user message
        prompt.append("\n\nUser:\n");
        prompt.append(state.getUserMessage());

        prompt.append("\n\nAssistant:");

        return prompt.toString();
    }
}