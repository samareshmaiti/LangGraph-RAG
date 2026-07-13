package com.chatbot.repository;

import com.chatbot.model.RegisterRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<RegisterRequest, UUID>{
    RegisterRequest findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<RegisterRequest> findByEmail(String email);
}
