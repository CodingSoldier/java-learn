package com.atguigu.study.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaaLLMConfig {

    private final String QWEN_MODEL = "qwen-plus";

    @Bean(name = "qwen")
    public ChatModel qwen() {
        DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(System.getenv("ALI_AI_KEY")).build();
        DashScopeChatOptions options = DashScopeChatOptions.builder().withModel(QWEN_MODEL).build();
        return DashScopeChatModel.builder().dashScopeApi(dashScopeApi).defaultOptions(options).build();
    }

    @Bean(name = "qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwen") ChatModel qwen,
                                     RedisChatMemoryRepository redisChatMemoryRepository) {
        MessageWindowChatMemory windowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(10)
                .build();
        ChatOptions options = ChatOptions.builder().model(QWEN_MODEL).build();
        return ChatClient.builder(qwen)
                .defaultOptions(options)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(windowChatMemory).build())
                .build();
    }

}
