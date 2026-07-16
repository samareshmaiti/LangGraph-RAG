package com.chatbot.ai.node;


import com.chatbot.ai.state.ChatState;
import com.chatbot.model.chatbot.ConversationMemory;
import com.chatbot.service.chatservice.ConversationMemoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bsc.langgraph4j.action.NodeAction;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemoryNode implements NodeAction<ChatState> {

    private final ConversationMemoryService memoryService;

    @Override
    public Map<String, Object> apply(ChatState state) {
        log.info("Loading conversation memory");

        ConversationMemory memory = memoryService.loadMemory(state.getConversationId(), state.getUsername());
        return Map.of( ChatState.CHAT_HISTORY, memory.messages(), ChatState.SUCCESS, true);

    }

}