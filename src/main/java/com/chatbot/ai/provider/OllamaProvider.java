package com.chatbot.ai.provider;

import com.chatbot.ai.state.ChatState;
import dev.langchain4j.model.chat.ChatModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OllamaProvider implements AiProvider {

    private final ChatModel chatModel;

    @Override
    public String generate(ChatState state) {

        log.info("Calling Ollama...");

        return chatModel.chat(state.getPrompt());
    }
}