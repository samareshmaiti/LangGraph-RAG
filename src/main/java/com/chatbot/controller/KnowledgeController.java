package com.chatbot.controller;


import com.chatbot.service.rag.KnowledgeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/knowledge")
@RequiredArgsConstructor
public class KnowledgeController {

    private final KnowledgeService knowledgeService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<String> uploadDocument(
            @RequestPart("file") MultipartFile file) {

        knowledgeService.upload(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Document indexed successfully.");
    }
}