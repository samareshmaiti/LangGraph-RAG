package com.chatbot.service.rag;

import dev.langchain4j.data.segment.TextSegment;

import java.util.List;

public interface ChunkingService {

    List<TextSegment> chunk(String text);

}