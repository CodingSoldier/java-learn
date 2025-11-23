package com.cpq.service;

import cn.hutool.json.JSONUtil;
import com.cpq.bean.ChatEntity;
import com.cpq.bean.ChatResponseEntity;
import com.cpq.enums.SSEMsgType;
import com.cpq.sse.SseServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ChatService{

    @Autowired
    private ChatClient chatClient;

    public void doChat(ChatEntity chatEntity) {

        String userId = chatEntity.getCurrentUserName();
        String prompt = chatEntity.getMessage();
        String botMsgId = chatEntity.getBotMsgId();

        Flux<String> stringFlux = chatClient.prompt(prompt).stream().content();

        List<String> list = stringFlux.toStream().map(str -> {
            String content = str.toString();
            SseServer.sendMsg(userId, content, SSEMsgType.ADD);
            log.info("content: {}", content);
            return content;
        }).collect(Collectors.toList());

        String fullContent = list.stream().collect(Collectors.joining());

        ChatResponseEntity chatResponseEntity = new ChatResponseEntity(fullContent, botMsgId);

        SseServer.sendMsg(userId, JSONUtil.toJsonStr(chatResponseEntity), SSEMsgType.FINISH);

    }

}
