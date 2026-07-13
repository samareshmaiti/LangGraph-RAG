package com.chatbot.ai.state;

import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatState extends MessagesState<String> {

    public static final String USERNAME = "username";
    public static final String CONVERSATION_ID = "conversationId";
    public static final String USER_MESSAGE = "userMessage";
    public static final String CONVERSATION_CONTEXT = "conversationContext";
    public static final String PROMPT = "prompt";
    public static final String ASSISTANT_MESSAGE = "assistantMessage";
    public static final String MODEL = "model";
    public static final String PROMPT_TOKENS = "promptTokens";
    public static final String COMPLETION_TOKENS = "completionTokens";
    public static final String TOTAL_TOKENS = "totalTokens";
    public static final String SUCCESS = "success";
    public static final String ERROR = "error";

    /**
     * Required by LangGraph4j
     */
    public ChatState() {
        super(new HashMap<>());
    }

    /**
     * Used by LangGraph
     */
    public ChatState(Map<String, Object> initData) {
        super(initData);
    }

    public UUID getConversationId() {
        return value(CONVERSATION_ID)
                .map(UUID.class::cast)
                .orElse(null);
    }

    public String getUsername() {
        return value(USERNAME)
                .map(String.class::cast)
                .orElse("");
    }

    public String getUserMessage() {
        return value(USER_MESSAGE)
                .map(String.class::cast)
                .orElse("");
    }

    public String getConversationContext() {
        return value(CONVERSATION_CONTEXT)
                .map(String.class::cast)
                .orElse("");
    }

    public String getPrompt() {
        return value(PROMPT)
                .map(String.class::cast)
                .orElse("");
    }

    public String getAssistantMessage() {
        return value(ASSISTANT_MESSAGE)
                .map(String.class::cast)
                .orElse("");
    }

    public String getModel() {
        return value(MODEL)
                .map(String.class::cast)
                .orElse("llama3.2");
    }

    public Integer getPromptTokens() {
        return value(PROMPT_TOKENS)
                .map(Integer.class::cast)
                .orElse(0);
    }

    public Integer getCompletionTokens() {
        return value(COMPLETION_TOKENS)
                .map(Integer.class::cast)
                .orElse(0);
    }

    public Integer getTotalTokens() {
        return value(TOTAL_TOKENS)
                .map(Integer.class::cast)
                .orElse(0);
    }

    public Boolean isSuccess() {
        return value(SUCCESS)
                .map(Boolean.class::cast)
                .orElse(true);
    }

    public String getError() {
        return value(ERROR)
                .map(String.class::cast)
                .orElse(null);
    }
}