package com.chatbot.ai.provider;

import com.chatbot.ai.provider.AiProvider;
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

        log.info("========== AI REQUEST START ==========");
        try {

            log.info("Prompt size: {}", state.getPrompt().length());
            long start = System.currentTimeMillis();
            String response = chatModel.chat(state.getPrompt());
            long time = System.currentTimeMillis() - start;

            log.info("AI RESPONSE RECEIVED in {} ms", time);

            return response;

        } catch (Exception e) {
            log.error("AI REQUEST FAILED", e);

            throw e;
        }
    }
}