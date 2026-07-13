package com.chatbot.service.chatservice;
import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.mapper.MessageMapper;
import com.chatbot.model.chatbot.Conversations;
import com.chatbot.model.chatbot.Message;
import com.chatbot.model.dto.request.MessageRequest;
import com.chatbot.model.dto.response.MessageResponse;
import com.chatbot.repository.ConversationRepository;
import com.chatbot.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MessageServiceImpl implements MessageService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageResponse saveUserMessage(MessageRequest request,
                                           String username) {

        Conversations conversation = null;
        try {
            conversation = conversationRepository
                    .findByIdAndUsername(request.getConversationId(), username)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Conversation not found", HttpStatus.NOT_FOUND.toString()));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        Message message = messageMapper.toUserMessage(request.getContent());

        message.setConversation(conversation);

        Message savedMessage = messageRepository.save(message);

        log.info("User message saved for conversation {}",
                conversation.getId());

        return messageMapper.toResponse(savedMessage);
    }

    @Override
    public MessageResponse saveAssistantMessage(UUID conversationId,
                                                String content,
                                                Integer promptTokens,
                                                Integer completionTokens,
                                                Integer totalTokens,
                                                String username) {

        Conversations conversation = null;
        try {
            conversation = conversationRepository
                    .findByIdAndUsername(conversationId, username)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Conversation not found", HttpStatus.NOT_FOUND.toString()));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        Message message = messageMapper.toAssistantMessage(
                content,
                promptTokens,
                completionTokens,
                totalTokens
        );

        message.setConversation(conversation);

        Message savedMessage = messageRepository.save(message);

        log.info("Assistant response saved for conversation {}",
                conversationId);

        return messageMapper.toResponse(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getConversationMessages(UUID conversationId,
                                                         String username) {

        try {
            conversationRepository.findByIdAndUsername(conversationId, username)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Conversation not found",HttpStatus.NOT_FOUND.toString()));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }

        return messageRepository
                .findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream()
                .map(messageMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteMessage(UUID messageId,
                              String username) throws ResourceNotFoundException {

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Message not found", HttpStatus.NOT_FOUND.toString()));

        if (!message.getConversation()
                .getUsername()
                .equals(username)) {

            throw new ResourceNotFoundException("Message not found",HttpStatus.NOT_FOUND.toString());
        }

        messageRepository.delete(message);

        log.info("Message deleted {}", messageId);
    }
}