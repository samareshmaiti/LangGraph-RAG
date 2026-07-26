package com.chatbot.ai.node.tools;

import com.chatbot.ai.state.ChatState;
import com.chatbot.model.chatbot.WeatherRequest;
import com.chatbot.util.WeatherExtractor;
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

    private static final String WEATHER_API =
            "http://wttr.in/%s?format=%C+%t+%h+%P+%w";

    private final WeatherExtractor weatherExtractor;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, Object> apply(ChatState state) {

        log.info("Weather Tool Started");

        String message = state.getUserMessage();

        if (!isWeatherQuestion(message)) {
            return Map.of();
        }

        WeatherRequest request =
                weatherExtractor.extract(message);

        String location = request.getLocation();

        if (location == null || location.isBlank()) {
            location = "Bangalore";
        }

        log.info("Location extracted = {}", location);

        try {

            String url = String.format(
                    WEATHER_API,
                    location.replace(" ", "+"));

            String weather =
                    restTemplate.getForObject(url, String.class);

            if (weather == null || weather.isBlank()) {
                weather = "Weather information unavailable.";
            }

            List<String> results =
                    new ArrayList<>(state.getToolResults());

            results.add(
                    "Weather for "
                            + location
                            + " : "
                            + weather);

            return Map.of(
                    ChatState.TOOL_RESULTS,
                    results
            );

        } catch (Exception ex) {

            log.error("Weather API Error", ex);

            List<String> results =
                    new ArrayList<>(state.getToolResults());

            results.add(
                    "Unable to fetch weather."
            );

            return Map.of(
                    ChatState.TOOL_RESULTS,
                    results
            );
        }
    }

    private boolean isWeatherQuestion(String message) {

        String m = message.toLowerCase();

        return m.contains("weather")
                || m.contains("temperature")
                || m.contains("forecast")
                || m.contains("rain")
                || m.contains("humidity")
                || m.contains("snow")
                || m.contains("wind")
                || m.contains("cloud");
    }
}