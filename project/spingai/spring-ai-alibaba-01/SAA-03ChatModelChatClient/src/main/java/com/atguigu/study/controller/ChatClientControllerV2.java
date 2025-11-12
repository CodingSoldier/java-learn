package com.atguigu.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatClientControllerV2 {

    @Resource
    private ChatClient client;

    @GetMapping(value = "/chat/client")
    public String doChat(@RequestParam(name = "msg",defaultValue="你是谁") String msg)
    {
        String result = client.prompt().user(msg).call().content();
        return result;
    }

}
