package com.chatbot.ai.node;

import com.chatbot.ai.node.tools.CodingNode;
import com.chatbot.ai.node.tools.GuardrailNode;
import com.chatbot.ai.node.tools.PlannerNode;
import com.chatbot.ai.node.tools.SummarizerNode;
import com.chatbot.ai.node.tools.WeatherNode;
import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class ToolExecutorNode implements NodeAction<ChatState> {

    private final WeatherNode weatherNode;
    private final CodingNode codingNode;
    private final GuardrailNode guardrailNode;
    private final PlannerNode plannerNode;
    private final SummarizerNode summarizerNode;

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Executing tool executor node with routing");

        // Start with current state
        Map<String, Object> result = new HashMap<>();

        // Get current tool results
        List<String> currentResults = state.getToolResults();
        if (currentResults == null) {
            currentResults = new ArrayList<>();
        }

        // Create a copy to modify
        List<String> updatedResults = new ArrayList<>(currentResults);

        // Determine which tools to execute based on routing
        Map<String, Boolean> executionMap = new HashMap<>();
        executionMap.put("weather", shouldExecuteTool(state, "weather"));
        executionMap.put("coding", shouldExecuteTool(state, "coding"));
        executionMap.put("guardrail", shouldExecuteTool(state, "guardrail"));
        executionMap.put("planner", shouldExecuteTool(state, "planner"));
        executionMap.put("summarizer", shouldExecuteTool(state, "summarizer"));

        // Execute only the tools that should run
        if (executionMap.get("weather")) {
            Map<String, Object> weatherResult = weatherNode.apply(state);
            mergeResults(weatherResult, updatedResults);
            log.debug("Executed weather tool");
        }

        if (executionMap.get("coding")) {
            Map<String, Object> codingResult = codingNode.apply(state);
            mergeResults(codingResult, updatedResults);
            log.debug("Executed coding tool");
        }

        if (executionMap.get("guardrail")) {
            Map<String, Object> guardrailResult = guardrailNode.apply(state);
            mergeResults(guardrailResult, updatedResults);
            log.debug("Executed guardrail tool");
        }

        if (executionMap.get("planner")) {
            Map<String, Object> plannerResult = plannerNode.apply(state);
            mergeResults(plannerResult, updatedResults);
            log.debug("Executed planner tool");
        }

        if (executionMap.get("summarizer")) {
            Map<String, Object> summarizerResult = summarizerNode.apply(state);
            mergeResults(summarizerResult, updatedResults);
            log.debug("Executed summarizer tool");
        }

        // If we have new results, update the state
        if (!updatedResults.equals(currentResults)) {
            result.put(ChatState.TOOL_RESULTS, new ArrayList<>(updatedResults));
            result.put(ChatState.SUCCESS, true);
            log.info("Updated tool results with {} new items", updatedResults.size() - currentResults.size());
        } else {
            // No changes
            result.put(ChatState.SUCCESS, true);
            log.info("No tool executions resulted in new information");
        }

        return result;
    }

    private boolean shouldExecuteTool(ChatState state, String toolName) {
        // Check if routing information is available from RouterNode
        Map<String, Object> routerResult = state.getRouterResult();
        if (routerResult != null && routerResult.containsKey("relevantTools")) {
            Object toolsObj = routerResult.get("relevantTools");
            if (toolsObj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> relevantTools = (List<String>) toolsObj;
                return relevantTools.contains(toolName);
            }
        }

        // Fallback: if no routing info, run safety-critical tools
        switch (toolName) {
            case "guardrail":
                return true; // Always run guardrail for safety
            default:
                return false; // For other tools, require explicit routing
        }
    }

    private void mergeResults(Map<String, Object> toolResult, List<String> currentResults) {
        if (toolResult != null && toolResult.containsKey(ChatState.TOOL_RESULTS)) {
            Object obj = toolResult.get(ChatState.TOOL_RESULTS);
            if (obj instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> list = (List<String>) obj;
                currentResults.addAll(list);
            }
        }
    }
}