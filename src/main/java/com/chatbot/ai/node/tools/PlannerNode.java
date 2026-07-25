package com.chatbot.ai.node.tools;

import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class PlannerNode implements NodeAction<ChatState> {

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Checking if planner tool is needed");

        String userMessage = state.getUserMessage().toLowerCase();
        // Check for planning-related keywords
        boolean isPlanningQuery = userMessage.contains("plan") ||
                userMessage.contains("steps") ||
                userMessage.contains("how to") ||
                userMessage.contains("strategy") ||
                userMessage.contains("approach") ||
                userMessage.contains("process") ||
                userMessage.contains("roadmap") ||
                userMessage.contains("organize");

        if (!isPlanningQuery) {
            log.info("Planning tool not needed");
            return Map.of();
        }

        log.info("Executing planning tool");

        // Create a simple plan based on the user message
        String plan = "Here's a suggested approach to tackle your request:\n" +
                "1. Clarify the objective and requirements\n" +
                "2. Break down the task into manageable sub-tasks\n" +
                "3. Identify necessary resources and information\n" +
                "4. Create a timeline or sequence of actions\n" +
                "5. Execute each step, monitoring progress\n" +
                "6. Review outcomes and adjust as needed\n" +
                "7. Finalize and deliver the result";

        // Append to existing tool results
        List<String> currentResults = state.getToolResults();
        List<String> updatedResults = new ArrayList<>();
        if (currentResults != null) {
            updatedResults.addAll(currentResults);
        }
        updatedResults.add(plan);

        return Map.of(
                ChatState.TOOL_RESULTS, updatedResults
        );
    }
}