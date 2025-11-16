package com.atguigu.study.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class McpClientCallBaiDuMcpController
{

    @Resource
    private ChatClient chatClient; //添加了MCP调用能力

    /**
     * 添加了MCP调用能力
     * http://localhost:8016/mcp/chat?msg=查询北纬39.9042东经116.4074天气
     * http://localhost:8016/mcp/chat?msg=查询61.149.121.66归属地
     * http://localhost:8016/mcp/chat?msg=查询昌平到天安门路线规划
     * @param msg
     * @return
     */
    @GetMapping("/mcp/chat")
    public Flux<String> mapChat(String msg) {
        return chatClient.prompt(msg).stream().content();
    }

}

