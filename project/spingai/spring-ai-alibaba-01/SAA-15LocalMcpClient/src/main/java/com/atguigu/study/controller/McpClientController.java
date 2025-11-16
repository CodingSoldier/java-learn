package com.atguigu.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class McpClientController {

    @Resource
    private ChatClient chatClient;

    // http://localhost:8015/mcpClient/chat?msg=上海
    @GetMapping("/mcpClient/chat")
    public Flux<String> chat(@RequestParam(name = "msg") String msg) {
        return chatClient.prompt(msg).stream().content();
    }

}
