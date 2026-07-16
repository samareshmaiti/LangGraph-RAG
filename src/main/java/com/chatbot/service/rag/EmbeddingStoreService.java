package com.chatbot.service.rag;

import com.chatbot.model.rag.KnowledgeDocument;
import dev.langchain4j.data.segment.TextSegment;

import java.util.List;

public interface EmbeddingStoreService {

    //void store(List<TextSegment> segments);

    List<TextSegment> search(String question, int maxResults);

    void store(KnowledgeDocument document, List<TextSegment> segments);



}