package com.chatbot.service.rag;


import com.chatbot.util.rag.DocumentProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class KnowledgeServiceImpl implements KnowledgeService {

    private final DocumentProcessor documentProcessor;

    private final ChunkingService chunkingService;

    private final EmbeddingStoreService embeddingStoreService;

    @Override
    public void upload(MultipartFile file) {

        try {

            log.info("Uploading document : {}", file.getOriginalFilename());

            // Step 1 : Extract text
            String text = documentProcessor.process(file);

            if (text == null || text.isBlank()) {
                throw new RuntimeException("Document contains no text.");
            }

            // Step 2 : Split into chunks
            var segments = chunkingService.chunk(text);

            if (segments.isEmpty()) {
                throw new RuntimeException("No chunks generated.");
            }

            // Step 3 : Store embeddings
            embeddingStoreService.store(segments);

            log.info("Successfully indexed {}", file.getOriginalFilename());

        } catch (Exception ex) {

            log.error("Error indexing document", ex);

            throw new RuntimeException(ex.getMessage(), ex);
        }
    }
}