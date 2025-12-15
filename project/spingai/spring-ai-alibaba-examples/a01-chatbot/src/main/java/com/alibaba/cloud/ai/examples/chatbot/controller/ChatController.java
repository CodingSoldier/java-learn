package com.alibaba.cloud.ai.examples.chatbot.controller;

import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("chat")
public class ChatController {


    @Resource
    private ReactAgent agent;

    /**
     * @Description: 聊天 + SSE返回
     */
    @PostMapping("doChat")
    public void doChat() throws Exception {
        AssistantMessage response = agent.call("分析这段文本：春天来了，万物复苏。");
// 输出会遵循 PoemOutput 的结构
        System.out.println(response.getText());
    }

}
