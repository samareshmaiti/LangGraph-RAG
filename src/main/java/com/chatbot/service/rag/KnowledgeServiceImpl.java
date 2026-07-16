package com.chatbot.service.rag;

import com.chatbot.model.rag.KnowledgeDocument;
import com.chatbot.repository.rag.DocumentKnowledgeRepo;
import com.chatbot.util.rag.DocumentProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KnowledgeServiceImpl implements KnowledgeService {

    private final DocumentProcessor documentProcessor;

    private final ChunkingService chunkingService;

    private final EmbeddingStoreService embeddingStoreService;

    private final DocumentKnowledgeRepo documentRepo;

    @Override
    public void upload(MultipartFile file) {

        try {

            log.info("Uploading document : {}", file.getOriginalFilename());

            // Prevent duplicate uploads
            if (documentRepo.existsByFileName(file.getOriginalFilename())) {
                throw new RuntimeException(
                        "Document already exists: " + file.getOriginalFilename());
            }

            // Save document metadata
            KnowledgeDocument document = KnowledgeDocument.builder()
                    .fileName(file.getOriginalFilename())
                    .contentType(file.getContentType())
                    .fileSize(file.getSize())
                    .uploadedBy("SYSTEM") // Replace with logged-in username
                    .uploadedAt(LocalDateTime.now())
                    .active(true)
                    .build();

            document = documentRepo.save(document);

            // Extract text
            String text = documentProcessor.process(file);

            if (text == null || text.isBlank()) {
                throw new RuntimeException("Document contains no text.");
            }

            // Split into chunks
            var segments = chunkingService.chunk(text);

            if (segments.isEmpty()) {
                throw new RuntimeException("No chunks generated.");
            }

            // Save chunks + embeddings
            embeddingStoreService.store(document, segments);

            log.info("Successfully indexed {}", file.getOriginalFilename());

        } catch (Exception ex) {

            log.error("Error indexing document", ex);

            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}