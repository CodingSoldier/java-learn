package com.cpq.controller;

import com.cpq.enums.SSEMsgType;
import com.cpq.sse.SseServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test/sse")
public class TestSseController {


    /**
     * @Description: SSE发送单个消息
     */
    @GetMapping("sendMessage")
    public Object sendMessage(@RequestParam String userId, @RequestParam String message){
        SseServer.sendMsg(userId, message, SSEMsgType.MESSAGE);
        return "OK";
    }

    /**
     * @Description: SSE发送单个消息 - add
     */
    @GetMapping("sendMessageAdd")
    public Object sendMessageAdd(@RequestParam String userId, @RequestParam String message) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(200);
            SseServer.sendMsg(userId, message, SSEMsgType.ADD);
        }
        return "OK";
    }

    /**
     * @Description: SSE发送群消息
     */
    @GetMapping("sendMessageAll")
    public Object sendMessageAll(@RequestParam String message){
        SseServer.sendMsgToAllUsers(message);
        return "OK";
    }

}
