package com.atguigu.ai.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class StreamOutputController
{

    @Resource(name="deepseekChatClient")
    private ChatClient deepseekChatClient;

    @Resource(name="qwenChatClient")
    private ChatClient qwenChatClient;

    @GetMapping(value = "/stringFluxDeepseek")
    public Flux<String> stringFluxDeepseek(@RequestParam(name = "msg",defaultValue="你是谁") String msg) {
        return deepseekChatClient.prompt(msg).stream().content();
    }

    @GetMapping(value = "/stringFluxQwen")
    public Flux<String> stringFluxQwen(@RequestParam(name = "msg",defaultValue="你是谁") String msg) {
        return qwenChatClient.prompt(msg).stream().content();
    }

}
