package com.atguigu.study.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashScopeConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Bean(name = "dashScopeAPi")
    public DashScopeApi dashScopeAPi() {
        // workSpaceId是阿里云百炼平台的业务空间ID
        // https://bailian.console.aliyun.com/?tab=app#/knowledge-base/detail/6aovdv07rm
        return DashScopeApi.builder()
                .apiKey(apiKey)
                .workSpaceId("llm-p01mpsp4k19pjldd")
                .build();
    }


    @Bean(name = "chatClient")
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }

}
