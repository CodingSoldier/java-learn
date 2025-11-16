package com.atguigu.ai.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * 知识出处，https://java2ai.com/docs/1.0.0.2/tutorials/basics/prompt/?spm=5176.29160081.0.0.2856aa5cdeol7a
 */
@RestController
public class PromptController
{
    @Resource(name = "deepseek")
    private ChatModel deepseekChatModel;
    @Resource(name = "qwen")
    private ChatModel qwenChatModel;

    @Resource(name = "deepseekChatClient")
    private ChatClient deepseekChatClient;
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("/prompt/system1")
    public Flux<String> chat(String question) {
        return deepseekChatClient.prompt()
                .system("你是一个法律助手，只回答法律问题，其它问题回复，我只能回答法律相关问题，其它无可奉告")
                .user(question)
                .stream()
                .content();
    }

    @GetMapping("/prompt/assistant")
    public String assistant(String question) {
        AssistantMessage assistantMessage = deepseekChatClient.prompt()
                .user(question)
                .call()
                .chatResponse()
                .getResult()
                .getOutput();
        return assistantMessage.getText();
    }

}
