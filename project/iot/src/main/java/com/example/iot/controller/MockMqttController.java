package com.example.iot.controller;

import com.example.iot.model.MockMqttReplyResponse;
import com.example.iot.model.MqttReplyMessage;
import com.example.iot.mqtt.MqttTopics;
import com.example.iot.service.ServiceInvokeReplyService;
import com.github.codingsoldier.common.resp.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于模拟 MQTT 回复消息的接口。
 */
@Slf4j
@RestController
@RequestMapping("/mock/mqtt")
@RequiredArgsConstructor
public class MockMqttController {

    private final ServiceInvokeReplyService serviceInvokeReplyService;

    /**
     * 模拟从 MQTT 调用回复主题收到回复。
     *
     * @param message 模拟的 MQTT 回复消息
     * @return 匹配结果
     */
    @PostMapping("/invoke-reply")
    public ResponseEntity<Result<MockMqttReplyResponse>> invokeReply(@Valid @RequestBody MqttReplyMessage message) {
        log.info("模拟 MQTT 接收，topic={}，msgId={}",
                MqttTopics.INVOKE_REPLY_TOPIC, message.getMsgId());
        boolean matched = serviceInvokeReplyService.completeReply(message.getMsgId(), message.getData());
        return ResponseEntity.ok(Result.success(MockMqttReplyResponse.builder()
                .msgId(message.getMsgId())
                .matched(matched)
                .build()));
    }
}
