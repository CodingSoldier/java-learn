package com.atguigu.study.controller;

import com.atguigu.study.utils.DateTimeTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ToolCallingController
{

    @Resource
    private ChatClient qwenChatClient;

    // http://localhost:8013/not/toolCalling?msg=你是谁，现在几点了
    @GetMapping("/not/toolCalling")
    public Flux<String> notToolCalling(String msg)
    {
        return qwenChatClient.prompt(msg)
                .stream()
                .content();
    }

    // http://localhost:8013/toolCalling?msg=你是谁，现在几点了
    @GetMapping("/toolCalling")
    public Flux<String> toolCalling(String msg)
    {
        return qwenChatClient.prompt(msg)
                .tools(new DateTimeTools())
                .stream()
                .content();
    }

}
