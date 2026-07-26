package com.chatbot.util;

import com.chatbot.model.chatbot.WeatherRequest;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface WeatherExtractor {

    @SystemMessage("""
        You are an information extraction assistant.

        Your job is to extract weather parameters from a user's message.

        Rules:
        - Extract only the location and date.
        - If the location is missing, return null.
        - If the date is missing, return "today".
        - Do not answer the question.
        - Return only valid JSON.

        Example:

        User: Weather in Bangalore tomorrow

        {
          "location":"Bangalore",
          "date":"tomorrow"
        }
        """)
    WeatherRequest extract(@UserMessage String message);
}