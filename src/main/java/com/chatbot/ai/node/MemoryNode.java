package com.chatbot.ai.node;

import com.chatbot.ai.state.ChatState;
import com.chatbot.exception.ResourceNotFoundException;
import com.chatbot.model.dto.response.MessageResponse;
import com.chatbot.service.chatservice.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemoryNode implements NodeAction<ChatState> {


    private final MessageService messageService;


    @Override
    public Map<String, Object> apply(ChatState state) throws Exception {

        List<MessageResponse> messages = messageService.getConversationMessages(
                        state.getConversationId(),
                        state.getUsername()
                );

        String context = messages.stream()
                        .map(message ->
                                message.getRole() + ": " + message.getContent())
                        .reduce("", (a, b) -> a + "\n" + b);

        return Map.of(ChatState.CONVERSATION_CONTEXT, context, ChatState.SUCCESS, true);
    }
}