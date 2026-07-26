package com.chatbot.ai.provider;


import com.chatbot.util.WeatherExtractor;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiServiceConfig {

    @Bean
    public WeatherExtractor weatherExtractor(ChatModel chatModel) {

        return AiServices.create(
                WeatherExtractor.class,
                chatModel
        );
    }
}