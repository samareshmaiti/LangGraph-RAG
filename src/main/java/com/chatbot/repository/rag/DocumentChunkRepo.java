package com.chatbot.repository.rag;

import com.chatbot.model.rag.DocumentChunk;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentChunkRepo extends JpaRepository<DocumentChunk,Long> {
    List<DocumentChunk> findByDocumentId(Long documentId);
}
