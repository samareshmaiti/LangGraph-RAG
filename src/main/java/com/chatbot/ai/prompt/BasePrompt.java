package com.chatbot.ai.prompt;

import com.chatbot.ai.state.ChatState;

public interface BasePrompt {
    String build(ChatState chatState);
}
