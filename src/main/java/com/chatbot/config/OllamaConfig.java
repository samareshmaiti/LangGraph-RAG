package com.chatbot.config;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class OllamaConfig {


    @Value("${ollama.base-url}")
    private String baseUrl;


    @Value("${ollama.model}")
    private String model;


    @Value("${ollama.api-key}")
    private String apiKey;


    @Bean
    public ChatModel chatModel() {

        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .modelName(model)
                .timeout(Duration.ofSeconds(120))
                .build();
    }
}