package com.example.iot.mqtt;

import com.example.iot.config.MqttProperties;
import com.example.iot.model.MqttPublishRequest;
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
public class HiveMqttGateway {

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
     * 发布 MQTT 消息。
     *
     * @param request 发布请求
     * @return 发送完成结果
     */
    public CompletableFuture<Void> publish(MqttPublishRequest request) {
        try {
            var builder = Mqtt5Publish.builder()
                    .topic(request.getTopic())
                    .qos(resolveQos())
                    .payload(request.getPayload());

            if (request.getResponseTopic() != null) {
                builder = builder.responseTopic(request.getResponseTopic());
            }
            if (request.getCorrelationData() != null) {
                builder = builder.correlationData(request.getCorrelationData());
            }

            Mqtt5Publish publish = builder.build();
            String msgId = "";
            try {
                byte[] correlationData = request.getCorrelationData();
                if (correlationData != null) {
                    msgId = new String(correlationData, StandardCharsets.UTF_8);
                }
            } catch (Exception ignored) {
                // 忽略解析异常
            }

            final String logMsgId = msgId;
            return mqttClient.publish(publish)
                    .thenApply(ignored -> (Void) null)
                    .whenComplete((ignored, throwable) -> {
                        if (throwable == null) {
                            log.info("MQTT 发布成功，topic={}，msgId={}", request.getTopic(), logMsgId);
                            return;
                        }
                        log.error("MQTT 发布失败，topic={}，msgId={}", request.getTopic(), logMsgId, throwable);
                    });
        } catch (RuntimeException ex) {
            log.error("序列化 MQTT 消息失败，topic={}", request.getTopic(), ex);
            return CompletableFuture.failedFuture(new IllegalStateException("序列化 MQTT 消息失败", ex));
        }
    }

    /**
     * 向指定主题发布 JSON payload。
     *
     * @param topic   目标主题
     * @param payload JSON payload 字节
     * @return 发送完成结果
     */
    public CompletableFuture<Void> publish(String topic, byte[] payload) {
        try {
            Mqtt5Publish publish = Mqtt5Publish.builder()
                    .topic(topic)
                    .qos(resolveQos())
                    .payload(payload)
                    .build();
            return mqttClient.publish(publish)
                    .thenApply(ignored -> (Void) null)
                    .whenComplete((ignored, throwable) -> {
                        if (throwable == null) {
                            log.info("MQTT 发布成功，topic={}", topic);
                            return;
                        }
                        log.error("MQTT 发布失败，topic={}", topic, throwable);
                    });
        } catch (RuntimeException ex) {
            log.error("构建 MQTT 发布消息失败，topic={}", topic, ex);
            return CompletableFuture.failedFuture(new IllegalStateException("构建 MQTT 发布消息失败", ex));
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
