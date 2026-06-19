package com.example.iot.mqtt;

import com.example.iot.model.MqttInvokeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

/**
 * 伪 MQTT 网关，仅记录发布日志，不连接真实 MQTT 服务。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PseudoMqttGateway implements MqttGateway {

    /**
     * 模拟的 MQTT 调用主题。
     */
    public static final String INVOKE_TOPIC = "/sys/servie/invoke";

    /**
     * 模拟的 MQTT 回复主题。
     */
    public static final String INVOKE_REPLY_TOPIC = "/sys/servie/invoke_reply";

    private final ObjectMapper objectMapper;

    /**
     * 记录一次模拟 MQTT 发布操作。
     *
     * @param message 调用消息
     */
    @Override
    public void sendInvoke(MqttInvokeMessage message) {
        try {
            String payload = objectMapper.writeValueAsString(message);
            log.info("模拟 MQTT 发布，topic={}，payload={}", INVOKE_TOPIC, payload);
        } catch (RuntimeException ex) {
            log.error("序列化模拟 MQTT 调用消息失败，msgId={}", message.getMsgId(), ex);
            throw new IllegalStateException("序列化模拟 MQTT 调用消息失败", ex);
        }
    }
}
