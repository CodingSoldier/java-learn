package com.atguigu.ai.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@RestController
public class PromptTemplateController
{
    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;


    @GetMapping("/promptTemplate")
    public Flux<String> promptTemplate(String topic, String outputFormat, String wordCount)
    {
        PromptTemplate promptTemplate = new PromptTemplate("""
                讲一个关于{topic}的故事，
                并以{outputFormat}格式输出，
                字数控制在{wordCount}左右
                """);
        Map<String, Object> map = Map.of("topic", topic,
                "outputFormat", outputFormat,
                "wordCount", wordCount);
        Prompt prompt = promptTemplate.create(map);
        return deepseekChatClient.prompt(prompt)
                .stream()
                .content();

    }


    @GetMapping("/promptTemplate2")
    public Flux<String> promptTemplate(String userTopic, String systemTopic)
    {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate("你是一个{systemTopic}助手，只回答{systemTopic}相关问题，其他问题不回答，以HTML格式输出结果");
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("systemTopic", systemTopic));

        PromptTemplate userPromptTemplate = new PromptTemplate("请介绍{userTopic}");
        Message userMessage = userPromptTemplate.createMessage(Map.of("userTopic", userTopic));

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

        return deepseekChatClient.prompt(prompt).stream().content();
    }




}
