package com.chatbot.ai.graph;

import com.chatbot.ai.node.MemoryNode;
import com.chatbot.ai.node.RetrievalNode;
import com.chatbot.ai.provider.AiProvider;
import com.chatbot.ai.state.ChatState;
import com.chatbot.ai.state.PromptTemplate;
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

    private final PromptTemplate promptTemplate;
    private final AiProvider aiProvider;
    private final MemoryNode memoryNode;
    private final RetrievalNode retrievalNode;

    public CompiledGraph<ChatState> build() throws Exception {

        AsyncNodeAction<ChatState> promptAction = state -> {

            String prompt = promptTemplate.build(state);

            return CompletableFuture.completedFuture(
                    Map.of(
                            ChatState.PROMPT, prompt,
                            ChatState.SUCCESS, true
                    )
            );
        };
        AsyncNodeAction<ChatState> retrievalAction = state -> {

            Map<String, Object> result;

            try {
                result = retrievalNode.apply(state);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return CompletableFuture.completedFuture(result);
        };
        AsyncNodeAction<ChatState> memoryAction = state -> {
            log.info("Executing memory node");

            Map<String,Object> result =
                    null;
            try {
                result = memoryNode.apply(state);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return CompletableFuture.completedFuture(result);
        };
        AsyncNodeAction<ChatState> llmAction = state -> {

            String response = aiProvider.generate(state);

            return CompletableFuture.completedFuture(
                    Map.of(
                            ChatState.ASSISTANT_MESSAGE, response,
                            ChatState.SUCCESS, true
                    )
            );
        };

        AsyncNodeAction<ChatState> responseAction = state -> {

            String response = state.getAssistantMessage();

            if (response == null) {
                response = "";
            }

            response = response.trim();

            return CompletableFuture.completedFuture(
                    Map.of(
                            ChatState.ASSISTANT_MESSAGE, response,
                            ChatState.SUCCESS, true
                    )
            );
        };

        return new StateGraph<>(ChatState.SCHEMA, ChatState::new)
                .addNode("memory",memoryAction)
                .addNode("retrieval", retrievalAction)
                .addNode("prompt", promptAction)
                .addNode("llm", llmAction)
                .addNode("response", responseAction)

                .addEdge(START,"memory")
                .addEdge("memory", "retrieval")
                .addEdge("retrieval", "prompt")
                .addEdge("prompt", "llm")
                .addEdge("llm", "response")
                .addEdge("response", END)

                .compile();
    }
}