package com.chatbot.ai.node.tools;

import com.chatbot.ai.state.ChatState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherNode implements NodeAction<ChatState> {

    private static final String WEATHER_API_URL = "http://wttr.in/%s?format=%C+%t+%h+%P+%w"; // Simple format
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Checking if weather tool is needed");

        String userMessage = state.getUserMessage().toLowerCase();
        // Check for weather-related keywords
        boolean isWeatherQuery = userMessage.contains("weather") ||
                userMessage.contains("temperature") ||
                userMessage.contains("forecast") ||
                userMessage.contains("rain") ||
                userMessage.contains("snow") ||
                userMessage.contains("sunny") ||
                userMessage.contains("cloudy") ||
                userMessage.contains("humidity");

        if (!isWeatherQuery) {
            log.info("Weather tool not needed");
            return Map.of();
        }

        log.info("Executing weather tool");

        // Extract location from user message (simple approach: look for capitalized words or after "in")
        String location = extractLocation(state.getUserMessage());
        if (location == null || location.isEmpty()) {
            location = "London"; // default location
        }

        try {
            String url = String.format(WEATHER_API_URL, location.replace(" ", "+"));
            String weatherInfo = restTemplate.getForObject(url, String.class);

            if (weatherInfo == null || weatherInfo.isEmpty()) {
                weatherInfo = "Weather data not available for " + location;
            }

            // Append to existing tool results
            List<String> currentResults = state.getToolResults();
            List<String> updatedResults = new ArrayList<>();
            if (currentResults != null) {
                updatedResults.addAll(currentResults);
            }
            String result = "Weather for " + location + ": " + weatherInfo.trim();
            updatedResults.add(result);

            return Map.of(
                    ChatState.TOOL_RESULTS, updatedResults
            );
        } catch (Exception e) {
            log.error("Error fetching weather data", e);
            List<String> currentResults = state.getToolResults();
            List<String> updatedResults = new ArrayList<>();
            if (currentResults != null) {
                updatedResults.addAll(currentResults);
            }
            updatedResults.add("Unable to retrieve weather information at this time.");

            return Map.of(
                    ChatState.TOOL_RESULTS, updatedResults
            );
        }
    }

    private String extractLocation(String message) {
        // Simple extraction: look for patterns like "in [location]" or "for [location]"
        String lowerMsg = message.toLowerCase();
        int inIndex = lowerMsg.indexOf(" in ");
        if (inIndex != -1) {
            // Extract substring after "in "
            String afterIn = message.substring(inIndex + 3);
            // Take first word or until punctuation
            int end = afterIn.length();
            for (int i = 0; i < afterIn.length(); i++) {
                char c = afterIn.charAt(i);
                if (c == ',' || c == '.' || c == '!' || c == '?' || c == ' ') {
                    end = i;
                    break;
                }
            }
            if (end > 0) {
                return afterIn.substring(0, end).trim();
            }
            return afterIn.trim();
        }

        int forIndex = lowerMsg.indexOf(" for ");
        if (forIndex != -1) {
            String afterFor = message.substring(forIndex + 5);
            int end = afterFor.length();
            for (int i = 0; i < afterFor.length(); i++) {
                char c = afterFor.charAt(i);
                if (c == ',' || c == '.' || c == '!' || c == '?' || c == ' ') {
                    end = i;
                    break;
                }
            }
            if (end > 0) {
                return afterFor.substring(0, end).trim();
            }
            return afterFor.trim();
        }

        return null;
    }
}