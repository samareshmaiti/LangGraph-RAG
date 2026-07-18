package com.chatbot.ai.prompt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PromptFactory {

    private final GeneralPrompt generalPrompt;
    private final MemoryPrompt memoryPrompt;
    private final RAGPrompt ragPrompt;
    private final ToolPrompt toolPrompt;

    public BasePrompt getPrompt(String agent) {

        return switch (agent.toUpperCase()) {

            case "MEMORY" -> memoryPrompt;

            case "RAG" -> ragPrompt;

            case "TOOL" -> toolPrompt;

            default -> generalPrompt;
        };
    }
}
