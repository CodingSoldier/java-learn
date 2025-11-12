package com.atguigu.study.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SaaLLMConfig
{
    private final String DEEPSEEK_MODEL = "deepseek-v3";
    private final String QWEN_MODEL = "qwen-max";

    @Bean("deepseekModel")
    public ChatModel deepseekModel()
    {
        DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(System.getenv("ALI_AI_KEY")).build();
        DashScopeChatOptions options = DashScopeChatOptions.builder().withModel(DEEPSEEK_MODEL).build();
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();
    }

    @Bean("qwenModel")
    public ChatModel qwenModel()
    {
        DashScopeApi dashScopeApi = DashScopeApi.builder().apiKey(System.getenv("ALI_AI_KEY")).build();
        DashScopeChatOptions options = DashScopeChatOptions.builder().withModel(QWEN_MODEL).build();
        return DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .defaultOptions(options)
                .build();
    }

    @Bean("deepseekChatClient")
    public ChatClient deepseekChatClient(@Qualifier("deepseekModel") ChatModel deepseekModel) {
        ChatOptions options = ChatOptions.builder().model(DEEPSEEK_MODEL).build();
        return ChatClient.builder(deepseekModel).defaultOptions(options).build();
    }

    @Bean("qwenChatClient")
    public ChatClient qwenChatClient(@Qualifier("qwenModel") ChatModel qwenModel) {
        ChatOptions options = ChatOptions.builder().model(QWEN_MODEL).build();
        return ChatClient.builder(qwenModel).defaultOptions(options).build();
    }

}