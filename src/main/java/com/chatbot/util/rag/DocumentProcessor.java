package com.chatbot.util.rag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DocumentProcessor {

    private final List<TextExtractor> extractors;

    public String process(MultipartFile file) throws IOException {

        String contentType = file.getContentType();

        return extractors.stream()
                .filter(extractor -> extractor.supports(contentType))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported file type: " + contentType))
                .extract(file);
    }
}