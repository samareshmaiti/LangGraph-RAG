package com.chatbot.ai.graph;

import com.chatbot.ai.node.*;
import com.chatbot.ai.provider.AiProvider;
import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.CompiledGraph;
import org.bsc.langgraph4j.StateGraph;
import org.bsc.langgraph4j.action.AsyncNodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.bsc.langgraph4j.GraphDefinition.END;
import static org.bsc.langgraph4j.GraphDefinition.START;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatGraph {

    private final MemoryNode memoryNode;
    private final RetrievalNode retrievalNode;
    private final PromptNode promptNode;
    private final RouterNode routerNode;
    private final AiProvider aiProvider;
    private final ResponseNode responseNode;
    private final LlmNode llmNode;
    private final ToolExecutorNode toolExecutorNode;

    public CompiledGraph<ChatState> build() throws Exception {

        AsyncNodeAction<ChatState> memoryAction = state ->
                CompletableFuture.completedFuture(memoryNode.apply(state));

        AsyncNodeAction<ChatState> retrievalAction = state ->
                CompletableFuture.completedFuture(retrievalNode.apply(state));

        AsyncNodeAction<ChatState> promptAction = state ->
                CompletableFuture.completedFuture(promptNode.apply(state));
        AsyncNodeAction<ChatState> routerAction =
                state -> CompletableFuture.completedFuture(routerNode.apply(state));
        AsyncNodeAction<ChatState> toolAction =
                state -> CompletableFuture.completedFuture(toolExecutorNode.apply(state));
        AsyncNodeAction<ChatState> responseAction =
                state -> CompletableFuture.completedFuture(responseNode.apply(state));
        AsyncNodeAction<ChatState> llmAction =
                state -> CompletableFuture.completedFuture(llmNode.apply(state));

        return new StateGraph<>(ChatState.SCHEMA, ChatState::new)

                .addNode("memory", memoryAction)
                .addNode("retrieval", retrievalAction)
                .addNode("prompt", promptAction)
                .addNode("router", routerAction)
                .addNode("tool", toolAction)
                .addNode("llm", llmAction)
                .addNode("response", responseAction)

                .addEdge(START, "memory")
                .addEdge("memory", "retrieval")
                .addEdge("retrieval", "prompt")
                .addEdge("prompt", "router")
                .addEdge("router", "tool")
                .addEdge("tool", "llm")
                .addEdge("llm", "response")
                .addEdge("response", END)

                .compile();
    }
}