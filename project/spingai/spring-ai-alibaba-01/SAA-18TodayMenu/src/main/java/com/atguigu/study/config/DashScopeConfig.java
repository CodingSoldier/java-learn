package com.atguigu.study.config;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashScopeConfig
{

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean
    public DashScopeApi dashScopeApi()
    {
        return DashScopeApi.builder()
                .apiKey(apiKey)
                .workSpaceId("llm-p01mpsp4k19pjldd")
                .build();
    }

    @Bean
    public ChatClient chatClient(ChatModel chatModel)
    {
        return ChatClient.builder(chatModel).build();
    }

    @Bean
    public DashScopeAgent dashScopeAgent(DashScopeAgentApi dashScopeAgentApi) {
        return new DashScopeAgent(dashScopeAgentApi);
    }


}
