package com.chatbot.service.rag;

import dev.langchain4j.data.segment.TextSegment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RetrievalServiceImpl implements RetrievalService {

    private static final int MAX_RESULTS = 5;

    private final EmbeddingStoreService embeddingStoreService;

    @Override
    public List<String> retrieve(String question) {

        log.info("Searching knowledge base...");

        List<TextSegment> segments =
                embeddingStoreService.search(question, MAX_RESULTS);

        List<String> context = segments.stream()
                .map(TextSegment::text)
                .toList();

        log.info("Retrieved {} chunks", context.size());

        return context;
    }
}