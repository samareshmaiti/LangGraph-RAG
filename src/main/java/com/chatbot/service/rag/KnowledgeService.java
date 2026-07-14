package com.chatbot.service.rag;

import org.springframework.web.multipart.MultipartFile;

public interface KnowledgeService {

    void upload(MultipartFile file);

}