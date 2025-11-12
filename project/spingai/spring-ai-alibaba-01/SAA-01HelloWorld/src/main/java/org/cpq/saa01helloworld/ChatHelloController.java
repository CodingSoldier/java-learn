package org.cpq.saa01helloworld;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatHelloController {

    @Resource
    private ChatModel chatModel;

    @GetMapping(value = "/hello/dochat")
    public String doChat(@RequestParam(name = "msg",defaultValue="你是谁") String msg)
    {
        String result = chatModel.call(msg);
        return result;
    }

    @GetMapping(value = "/hello/streamchat")
    public Flux<String> streamchat(@RequestParam(name = "msg",defaultValue="你是谁") String msg)
    {
        Flux<String> result = chatModel.stream(msg);
        return result;
    }
}
