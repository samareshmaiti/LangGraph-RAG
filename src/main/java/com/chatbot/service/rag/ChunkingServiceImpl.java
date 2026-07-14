package com.chatbot.service.rag;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ChunkingServiceImpl implements ChunkingService {

    /**
     * maxSegmentSize = 500 characters
     * maxOverlapSize = 100 characters
     */
    private static final int MAX_SEGMENT_SIZE = 500;
    private static final int MAX_OVERLAP_SIZE = 100;

    @Override
    public List<TextSegment> chunk(String text) {

        Document document = Document.from(text);

        DocumentByParagraphSplitter splitter =
                new DocumentByParagraphSplitter(
                        MAX_SEGMENT_SIZE,
                        MAX_OVERLAP_SIZE
                );

        List<TextSegment> segments = splitter.split(document);

        log.info("Created {} chunks", segments.size());

        return segments;
    }
}