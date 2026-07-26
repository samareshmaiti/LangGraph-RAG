package com.chatbot.ai.state;

import com.chatbot.model.dto.response.MessageResponse;
import org.bsc.langgraph4j.prebuilt.MessagesState;

import java.util.HashMap;
import java.util.List;
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
    public static final String CHAT_HISTORY = "chatHistory";
    public static final String RETRIEVED_CONTEXT = "retrievedContext";
    public static final String TOOL_RESULTS = "toolResults";
    public static final String CURRENT_AGENT = "currentAgent";
    public static final String ROUTER_RESULT = "routerResult";
    public static final String ORIGINAL_USER_MESSAGE = "originalUserMessage";
    public static final String IS_CLASSIFICATION_TASK = "isClassificationTask";

    //Constructor, Required by LangGraph4j
    public ChatState() {super(new HashMap<>());}

    //state creation--- Used while creating state
    public ChatState(Map<String, Object> initData) {super(initData);}
    public void put(String key, Object value) {data().put(key, value);}
    public void putAll(Map<String, Object> values) {data().putAll(values);}

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

    @SuppressWarnings("unchecked")
    public List<String> getChatHistory() {
        return value(CHAT_HISTORY)
                .map(v -> (List<String>) v)
                .orElse(List.of());
    }

    @SuppressWarnings("unchecked")
    public List<String> getRetrievedContext() {
        return value(RETRIEVED_CONTEXT)
                .map(v -> (List<String>) v)
                .orElse(List.of());
    }


    @SuppressWarnings("unchecked")
    public List<String> getToolResults() {
        return value(TOOL_RESULTS)
                .map(v -> (List<String>) v)
                .orElse(List.of());
    }

    public String getCurrentAgent() {
        return value(CURRENT_AGENT)
                .map(String.class::cast)
                .orElse("GENERAL");
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getRouterResult() {
        return value(ROUTER_RESULT)
                .map(v -> (Map<String, Object>) v)
                .orElse(null);
    }

    public String getOriginalUserMessage() {
        return value(ORIGINAL_USER_MESSAGE)
                .map(String.class::cast)
                .orElse(null);
    }

    public Boolean getIsClassificationTask() {
        return value(IS_CLASSIFICATION_TASK)
                .map(Boolean.class::cast)
                .orElse(false);
    }

    public void setRouterResult(Map<String, Object> routerResult) {
        put(ROUTER_RESULT, routerResult);
    }

    public void setChatHistory(List<String> history) {put(CHAT_HISTORY, history);}
    public void setConversationId(UUID conversationId) {put(CONVERSATION_ID, conversationId);}
    public void setUsername(String username) {
        put(USERNAME, username);
    }
    public void setUserMessage(String userMessage) {
        put(USER_MESSAGE, userMessage);
    }
    public void setConversationContext(String context) {
        put(CONVERSATION_CONTEXT, context);
    }
    public void setPrompt(String prompt) {
        put(PROMPT, prompt);
    }
    public void setAssistantMessage(String assistantMessage) {
        put(ASSISTANT_MESSAGE, assistantMessage);
    }
    public void setModel(String model) {
        put(MODEL, model);
    }
    public void setPromptTokens(Integer promptTokens) {put(PROMPT_TOKENS, promptTokens);}
    public void setCompletionTokens(Integer completionTokens) {
        put(COMPLETION_TOKENS, completionTokens);
    }
    public void setTotalTokens(Integer totalTokens) {
        put(TOTAL_TOKENS, totalTokens);
    }
    public void setSuccess(Boolean success) {
        put(SUCCESS, success);
    }
    public void setError(String error) {
        put(ERROR, error);
    }
    public void setRetrievedContext(List<String> context) {
        put(RETRIEVED_CONTEXT, context);
    }
    public void setToolResults(List<String> results) {put(TOOL_RESULTS, results);}
    public void setCurrentAgent(String agent) {
        put(CURRENT_AGENT, agent);
    }
    public void setOriginalUserMessage(String originalUserMessage) {
        put(ORIGINAL_USER_MESSAGE, originalUserMessage);
    }
    public void setIsClassificationTask(Boolean isClassificationTask) {
        put(IS_CLASSIFICATION_TASK, isClassificationTask);
    }
}
