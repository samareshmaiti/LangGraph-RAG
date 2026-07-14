package com.chatbot.util.rag;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TextExtractor {

    boolean supports(String contentType);

    String extract(MultipartFile file) throws IOException;
}