package com.chatbot.service.rag;

import java.util.List;

public interface RetrievalService {

    List<String> retrieve(String question);

}