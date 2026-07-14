package com.chatbot.ai.state;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PromptTemplate {

    private static final String SYSTEM_PROMPT = """
            You are ChatBot, a professional AI assistant.

            Follow these instructions carefully:

            1. Answer the user's question accurately and professionally.

            2. If relevant information is available in the Knowledge Base,
               prioritize it over your general knowledge.

            3. If the Knowledge Base does not contain the answer,
               answer using your own knowledge and clearly state that the
               information was not found in the uploaded documents.

            4. Use Conversation History to maintain context across messages.

            5. Use Tool Results whenever they are available, as they contain
               real-time or external information and should take precedence.

            6. Never fabricate facts or references.

            7. If you are uncertain, clearly mention the uncertainty.

            8. Keep answers concise unless the user explicitly asks for details.

            9. Format responses using Markdown whenever appropriate.

            10. Be polite, helpful and conversational.
            """;

    public String build(ChatState state) {

        StringBuilder prompt = new StringBuilder();

        // =====================================================
        // System Prompt
        // =====================================================

        prompt.append(SYSTEM_PROMPT);

        prompt.append("\n\nCurrent Date: ")
                .append(LocalDate.now());

        // =====================================================
        // Retrieved Knowledge (RAG)
        // =====================================================

        if (!state.getRetrievedContext().isEmpty()) {

            prompt.append("""

                    ==================================================
                    KNOWLEDGE BASE
                    ==================================================
                    """);

            state.getRetrievedContext()
                    .forEach(chunk ->
                            prompt.append("- ")
                                    .append(chunk)
                                    .append("\n")
                    );
        }

        // =====================================================
        // Conversation History
        // =====================================================

        if (!state.getChatHistory().isEmpty()) {

            prompt.append("""

                    ==================================================
                    CONVERSATION HISTORY
                    ==================================================
                    """);

            state.getChatHistory()
                    .forEach(history ->
                            prompt.append(history)
                                    .append("\n"));
        }

        // Fallback if history is stored as plain context

        else if (state.getConversationContext() != null &&
                !state.getConversationContext().isBlank()) {

            prompt.append("""

                    ==================================================
                    CONVERSATION CONTEXT
                    ==================================================
                    """);

            prompt.append(state.getConversationContext())
                    .append("\n");
        }

        // =====================================================
        // Tool Results
        // =====================================================

        if (!state.getToolResults().isEmpty()) {

            prompt.append("""

                    ==================================================
                    TOOL RESULTS
                    ==================================================
                    """);

            state.getToolResults()
                    .forEach(result ->
                            prompt.append(result)
                                    .append("\n"));
        }

        // =====================================================
        // User Question
        // =====================================================

        prompt.append("""

                ==================================================
                USER QUESTION
                ==================================================
                """);

        prompt.append(state.getUserMessage());

        prompt.append("""

                
                
                ==================================================
                ASSISTANT RESPONSE
                ==================================================
                """);

        return prompt.toString();
    }
}