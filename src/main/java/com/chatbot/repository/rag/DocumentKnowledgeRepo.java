package com.chatbot.repository.rag;

import com.chatbot.model.rag.KnowledgeDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentKnowledgeRepo extends JpaRepository<KnowledgeDocument,Long> {
    Optional<KnowledgeDocument> findByFileName(String fileName);

    boolean existsByFileName(String fileName);
}
