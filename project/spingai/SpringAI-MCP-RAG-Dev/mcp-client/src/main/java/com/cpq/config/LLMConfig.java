package com.cpq.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory, ToolCallbackProvider tools) {
        /**
         * defaultToolCallbacks(tools)  注入ToolCallbackProvider，使用mcp
         * ChatClient配置defaultOptions可能导致无法使用远程mcp
         */
        return ChatClient.builder(chatModel)
                .defaultToolCallbacks(tools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

}