package com.chatbot.ai.graph;

import com.chatbot.ai.graph.ChatGraph;
import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import org.bsc.langgraph4j.CompiledGraph;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GraphConfig {

    private final ChatGraph chatGraph;

    @Bean
    public CompiledGraph<ChatState> compiledChatGraph() throws Exception {
        return chatGraph.build();
    }
}