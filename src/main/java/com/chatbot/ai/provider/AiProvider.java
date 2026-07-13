package com.chatbot.ai.provider;

import com.chatbot.ai.state.ChatState;

public interface AiProvider {

    String generate(ChatState state);

}