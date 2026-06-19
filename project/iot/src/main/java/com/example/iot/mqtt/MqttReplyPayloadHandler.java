package com.example.iot.mqtt;

import com.example.iot.model.MqttReplyMessage;
import com.example.iot.service.ServiceInvokeReplyService;
import com.github.codingsoldier.common.util.objectmapper.ObjectMapperUtil;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MQTT 回复载荷处理器。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqttReplyPayloadHandler {

    private final ServiceInvokeReplyService serviceInvokeReplyService;

    /**
     * 解析并处理 MQTT 回复载荷。
     *
     * @param payload MQTT 回复载荷
     */
    public void handle(byte[] payload) {
        MqttReplyMessage message;
        try {
            message = ObjectMapperUtil.readValue(payload, MqttReplyMessage.class);
        } catch (RuntimeException ex) {
            log.error("解析 MQTT 回复异常，topic={}，payload={}",
                    MqttTopics.INVOKE_REPLY_TOPIC, new String(payload, StandardCharsets.UTF_8), ex);
            return;
        }
        log.info("MQTT 收到回复数据，topic={}，msgId={}，message={}",
            MqttTopics.INVOKE_REPLY_TOPIC, message.getMsgId(), message);

        if (message == null) {
            log.error("解析 MQTT 回复失败，message == null");
            return;
        }
        if (message.getMsgId() == null) {
            log.warn("MQTT 回复缺少 msgId，topic={}", MqttTopics.INVOKE_REPLY_TOPIC);
            return;
        }

        boolean matched = serviceInvokeReplyService.completeReply(message.getMsgId(), message.getData());
        log.info("MQTT 回复处理完成，topic={}，msgId={}，matched={}",
                MqttTopics.INVOKE_REPLY_TOPIC, message.getMsgId(), matched);
    }
}
