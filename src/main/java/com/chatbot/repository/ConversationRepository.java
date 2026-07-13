package com.chatbot.repository;
import com.chatbot.model.chatbot.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConversationRepository extends JpaRepository<Conversations, UUID> {

    List<Conversations> findByUsernameOrderByUpdatedAtDesc(String username);

    Optional<Conversations> findByIdAndUsername(UUID id, String username);

    boolean existsByIdAndUsername(UUID id, String username);
}