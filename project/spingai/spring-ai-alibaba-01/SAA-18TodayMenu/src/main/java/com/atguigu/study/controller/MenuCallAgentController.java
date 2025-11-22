package com.atguigu.study.controller;

import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgent;
import com.alibaba.cloud.ai.dashscope.agent.DashScopeAgentOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


@RestController
public class MenuCallAgentController
{

    @Value("${spring.ai.dashscope.agent.options.app-id}")
    private String appId;

    @Autowired
    private DashScopeAgent dashScopeAgent;

    @GetMapping(value = "/eatAgent", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> eatAgent(@RequestParam(name = "msg", defaultValue = "今天吃什么") String msg) {
        DashScopeAgentOptions options = DashScopeAgentOptions.builder().withAppId(appId).build();
        Prompt prompt = new Prompt(msg, options);
        return dashScopeAgent.stream(prompt).map(response -> response.getResult().getOutput().getText());
    }

}
