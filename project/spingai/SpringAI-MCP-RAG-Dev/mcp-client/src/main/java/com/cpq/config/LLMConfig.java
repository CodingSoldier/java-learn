package com.cpq.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LLMConfig {

    @Value("${spring.ai.openai.chat.options.model}")
    private String model;

    @Bean
    public ChatClient chatClient(ChatModel chatModel, ToolCallbackProvider tools) {
        return ChatClient.builder(chatModel)
                .defaultOptions(ChatOptions.builder()
                        .model(model)
                        .build())
                .defaultToolCallbacks(tools) // 注入ToolCallbackProvider，使用mcp
                .build();
    }

}