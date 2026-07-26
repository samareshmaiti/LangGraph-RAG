package com.chatbot.ai.prompt;

import com/chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptFactory {

    private final GeneralPrompt generalPrompt;
    private final MemoryPrompt memoryPrompt;
    private final RAGPrompt ragPrompt;
    private final ToolPrompt toolPrompt;
    private final ClassificationPrompt classificationPrompt;

    public BasePrompt getPrompt(String agent) {
        return switch (agent.toUpperCase()) {
            case "MEMORY" -> memoryPrompt;
            case "RAG" -> ragPrompt;
            case "TOOL" -> toolPrompt;
            default -> generalPrompt;
        };
    }
    
    /**
     * Special method to get the classification prompt when we're doing agent classification
     */
    public BasePrompt getClassificationPrompt() {
        return classificationPrompt;
    }
}
