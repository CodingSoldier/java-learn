package com.example.iot.mqtt;

import com.example.iot.config.MqttProperties;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * HiveMQ MQTT 回复主题订阅器。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HiveMqttReplySubscriber {

    private final Mqtt5AsyncClient mqttClient;

    private final MqttProperties properties;

    private final MqttReplyPayloadHandler mqttReplyPayloadHandler;

    /**
     * 订阅服务调用回复主题。
     *
     * @return 订阅完成结果
     */
    public CompletableFuture<Void> subscribeReplyTopic() {
        Mqtt5Subscribe subscribe = Mqtt5Subscribe.builder()
                .addSubscription()
                .topicFilter(MqttTopics.INVOKE_REPLY_TOPIC)
                .qos(resolveQos())
                .applySubscription()
                .build();
        return mqttClient.subscribe(subscribe, publish ->
                        mqttReplyPayloadHandler.handle(publish.getPayloadAsBytes()))
                .thenApply(ignored -> null);
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
