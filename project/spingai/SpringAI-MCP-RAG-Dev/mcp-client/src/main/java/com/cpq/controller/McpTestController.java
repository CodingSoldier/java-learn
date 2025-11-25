package com.cpq.controller;

import com.cpq.bean.ChatEntity;
import com.cpq.service.McpService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mcp-test")
public class McpTestController {

    @Resource
    private McpService mcpService;


    /**
     1、测试本地mcp文件服务、远程mcp高德地图服务，请求参数：
        curl --location --request POST 'http://127.0.0.1:9090/mcp-test/doChat' \
        --header 'Content-Type: application/json' \
        --data-raw '{
            "currentUserName": 12456,
            "message": "请在spring-ai-cmp目录下生成一个abc.html文件，把深圳一天旅游攻略写入到abc.html，使用好看的css+html来编写",
            "botMsgId": 154545475454
        }'
     但是无法在本地创建文件，提示没有权限
     */
    @PostMapping("doChat")
    public void doChat(@RequestBody ChatEntity chatEntity){
        mcpService.doChat(chatEntity);
    }

}
