package com.atguigu.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatMemory4RedisController
{
    @Resource(name = "qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping("/memory/chat")
    public String memoryChat(String question, String userId)
    {
        return qwenChatClient.prompt(question)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, userId))
                .call()
                .content();
    }

}
