package com.chatbot.service.chatservice;

import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.mapper.ConversationMapper;
import com.chatbot.model.chatbot.Conversations;
import com.chatbot.model.dto.request.CreateConversationRequest;
import com.chatbot.model.dto.response.ConversationResponse;
import com.chatbot.repository.ConversationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ConversationServiceImpl implements ConversationService {

    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;

    @Override
    public UUID createConversation(CreateConversationRequest request, String username) {

        Conversations conversation =
                Conversations.builder()
                        .username(username)
                        .title(request.getTitle())
                        .active(true)
                        .build();

        return conversationRepository
                .save(conversation)
                .getId();
    }

    @Override
    @Transactional(readOnly = true)
    public ConversationResponse getConversation(UUID conversationId,
                                                String username) throws ResourceNotFoundException {

        Conversations conversation =
                conversationRepository
                        .findByIdAndUsername(conversationId, username)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Conversation not found", HttpStatus.NOT_FOUND.toString()));

        return conversationMapper.toResponse(conversation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConversationResponse> getUserConversations(String username) {

        return conversationRepository
                .findByUsernameOrderByUpdatedAtDesc(username)
                .stream()
                .map(conversationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ConversationResponse updateConversation(UUID conversationId,
                                                   CreateConversationRequest request,
                                                   String username) {

        Conversations conversation =
                null;
        try {
            conversation = conversationRepository
                    .findByIdAndUsername(conversationId, username)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Conversation not found", HttpStatus.NOT_FOUND.toString()));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        conversation.setTitle(request.getTitle());
        conversation.setUpdatedAt(LocalDateTime.now());

        Conversations updatedConversation =
                conversationRepository.save(conversation);

        log.info("Conversation updated : {}", conversationId);

        return conversationMapper.toResponse(updatedConversation);
    }

    @Override
    public void deleteConversation(UUID conversationId,
                                   String username) {

        Conversations conversation =
                null;
        try {
            conversation = conversationRepository
                    .findByIdAndUsername(conversationId, username)
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Conversation not found", HttpStatus.NOT_FOUND.toString()));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        conversationRepository.delete(conversation);

        log.info("Conversation deleted : {}", conversationId);
    }
}