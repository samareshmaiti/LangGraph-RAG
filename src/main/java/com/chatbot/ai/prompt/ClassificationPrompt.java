package com.chatbot.ai.prompt;

import com.chatbot.ai.state.ChatState;
import org.springframework.stereotype.Component;

@Component
public class ClassificationPrompt implements BasePrompt {

    @Override
    public String build(ChatState state) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("""
                You are an AI agent classifier. Your task is to determine which type of agent should handle the user's question.
                
                Available agent types:
                1. MEMORY - Use when the user is asking about previous conversation, chat history, or what was discussed earlier
                2. RAG - Use when the user is asking about information from documents, knowledge base, or specific reference materials
                3. TOOL - Use when the user needs to use external tools like weather, code execution, calculations, translations, etc.
                4. GENERAL - Use for general conversation, greetings, or when none of the above apply
                
                Instructions:
                - Analyze the user's question below
                - Respond with ONLY the agent type (MEMORY, RAG, TOOL, or GENERAL)
                - Do not add any explanation or additional text
                - Choose the most appropriate agent type based on the question
                
                User Question:
                """);

        // Get the original user message if we're in classification mode
        String userMessage = state.getUserMessage();
        if (userMessage == null) {
            // Fallback to the regular user message if original not set
            userMessage = state.getUserMessage();
        }
        prompt.append(userMessage);
        prompt.append("\n\nAgent Type:");

        return prompt.toString();
    }
}
