package com.example.iot.mqtt;

import com.example.iot.config.MqttProperties;
import com.example.iot.model.MqttInvokeMessage;
import com.github.codingsoldier.common.util.objectmapper.ObjectMapperUtil;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 基于 HiveMQ Client 的真实 MQTT 发布网关。
 */
@Slf4j
@Service
public class HiveMqttGateway implements MqttGateway {

    private final Mqtt5AsyncClient mqttClient;

    private final MqttProperties properties;

    /**
     * 创建真实 MQTT 网关。
     *
     * @param mqttClient MQTT 5 异步客户端
     * @param properties MQTT 配置
     */
    @Autowired
    public HiveMqttGateway(Mqtt5AsyncClient mqttClient, MqttProperties properties) {
        this.mqttClient = mqttClient;
        this.properties = properties;
    }

    /**
     * 向 MQTT 调用主题发布服务调用消息。
     *
     * @param message 调用消息
     * @return 发送完成结果
     */
    @Override
    public CompletableFuture<Void> sendInvoke(MqttInvokeMessage message) {
        try {
            byte[] payload = ObjectMapperUtil.writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
            Mqtt5Publish publish = Mqtt5Publish.builder()
                    .topic(MqttTopics.INVOKE_TOPIC)
                    .qos(resolveQos())
                    .payload(payload)
                    .build();
            return mqttClient.publish(publish)
                    .thenApply(ignored -> (Void) null)
                    .whenComplete((ignored, throwable) -> {
                        if (throwable == null) {
                            log.info("MQTT 发布成功，topic={}，msgId={}",
                                    MqttTopics.INVOKE_TOPIC, message.getMsgId());
                            return;
                        }
                        log.error("MQTT 发布失败，topic={}，msgId={}",
                                MqttTopics.INVOKE_TOPIC, message.getMsgId(), throwable);
                    });
        } catch (RuntimeException ex) {
            log.error("序列化 MQTT 调用消息失败，msgId={}", message.getMsgId(), ex);
            return CompletableFuture.failedFuture(new IllegalStateException("序列化 MQTT 调用消息失败", ex));
        }
    }

    private MqttQos resolveQos() {
        MqttQos qos = MqttQos.fromCode(properties.getQos());
        if (qos != null) {
            return qos;
        }
        log.warn("MQTT QoS 配置非法，qos={}，使用默认 QoS 1", properties.getQos());
        return MqttQos.AT_LEAST_ONCE;
    }
}
