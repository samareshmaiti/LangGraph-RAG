package com.chatbot.util.rag;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MarkdownTextExtractor implements TextExtractor {

    @Override
    public boolean supports(String contentType) {

        return "text/markdown".equals(contentType);
    }

    @Override
    public String extract(MultipartFile file) throws IOException {

        return new String(file.getBytes(), StandardCharsets.UTF_8);
    }
}