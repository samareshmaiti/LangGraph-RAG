package com.chatbot.ai.provider;

import com.chatbot.ai.state.ChatState;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.response.ChatResponse;
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

            ChatRequest request = ChatRequest.builder()
                    .messages(
                            SystemMessage.from(state.getPrompt()),
                            UserMessage.from(state.getUserMessage())
                    )
                    .build();

            long start = System.currentTimeMillis();

            ChatResponse response = chatModel.chat(request);

            long time = System.currentTimeMillis() - start;

            log.info("AI RESPONSE RECEIVED in {} ms", time);

            return response.aiMessage().text();

        } catch (Exception e) {

            log.error("AI REQUEST FAILED", e);
            throw e;
        }
    }
}