package com.example.iot.mqtt;

import com.example.iot.model.MqttInvokeMessage;
import com.github.codingsoldier.common.util.objectmapper.ObjectMapperUtil;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;

/**
 * 伪 MQTT 网关，仅记录发布日志，不连接真实 MQTT 服务。
 */
@Slf4j
public class PseudoMqttGateway implements MqttGateway {

    /**
     * 记录一次模拟 MQTT 发布操作。
     *
     * @param message 调用消息
     */
    @Override
    public CompletableFuture<Void> sendInvoke(MqttInvokeMessage message) {
        try {
            String payload = ObjectMapperUtil.writeValueAsString(message);
            log.info("模拟 MQTT 发布，topic={}，payload={}", MqttTopics.INVOKE_TOPIC, payload);
            return CompletableFuture.completedFuture(null);
        } catch (RuntimeException ex) {
            log.error("序列化模拟 MQTT 调用消息失败，msgId={}", message.getMsgId(), ex);
            throw new IllegalStateException("序列化模拟 MQTT 调用消息失败", ex);
        }
    }
}
