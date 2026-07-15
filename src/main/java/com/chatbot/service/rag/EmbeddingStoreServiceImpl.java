package com.chatbot.service.rag;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmbeddingStoreServiceImpl implements EmbeddingStoreService {

    private final EmbeddingModel embeddingModel;

    private final EmbeddingStore<TextSegment> embeddingStore;

    @PostConstruct
    public void init() {
        log.info("EmbeddingModel implementation = {}",
                embeddingModel.getClass().getName());
    }
    @Override
    public void store(List<TextSegment> segments) {

        if (segments == null || segments.isEmpty()) {
            return;
        }

        log.info("Generating embeddings for {} chunks", segments.size());

        List<Embedding> embeddings = embeddingModel
                .embedAll(segments)
                .content();

        embeddingStore.addAll(embeddings, segments);

        log.info("Successfully stored {} embeddings", embeddings.size());
    }

    @Override
    public List<TextSegment> search(String question, int maxResults) {

        Embedding queryEmbedding = embeddingModel
                .embed(question)
                .content();

        EmbeddingSearchRequest request = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(maxResults)
                .build();

        EmbeddingSearchResult<TextSegment> result =
                embeddingStore.search(request);

        return result.matches()
                .stream()
                .map(EmbeddingMatch::embedded)
                .toList();
    }
}