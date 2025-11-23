package com.cpq.controller;

import com.cpq.bean.ChatEntity;
import com.cpq.service.ChatService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("chat")
public class ChatController {


    @Resource
    private ChatService chatService;

    /**
     * @Description: 聊天 + SSE返回
     */
    @PostMapping("doChat")
    public void doChat(@RequestBody ChatEntity chatEntity){
        chatService.doChat(chatEntity);
    }

}
