package com.chatbot.repository;


import com.chatbot.model.chatbot.Message;
import com.chatbot.util.MessageRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> findByConversationIdOrderByCreatedAtAsc(UUID conversationId);

    List<Message> findByConversationIdAndRole(UUID conversationId,
                                              MessageRole role);

    void deleteByConversationId(UUID conversationId);
}