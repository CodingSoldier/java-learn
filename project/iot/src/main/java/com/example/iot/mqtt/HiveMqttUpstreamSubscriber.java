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
 * HiveMQ MQTT 上游消息订阅器。
 * <p>
 * 订阅直连设备和网关子设备的服务响应通配符 Topic。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HiveMqttUpstreamSubscriber {

    private final Mqtt5AsyncClient mqttClient;

    private final MqttProperties properties;

    private final MqttUpstreamDispatcher mqttUpstreamDispatcher;

    /**
     * 订阅直连设备和网关子设备的服务响应 Topic。
     *
     * @return 订阅完成结果
     */
    public CompletableFuture<Void> subscribeServiceResponseTopics() {
        String directFilter = MqttTopicResolver.directServiceResponseFilter();
        String gatewayFilter = MqttTopicResolver.gatewayServiceResponseFilter();

        return subscribe(directFilter)
                .thenCompose(ignored -> subscribe(gatewayFilter))
                .whenComplete((ignored, throwable) -> {
                    if (throwable == null) {
                        log.info("服务响应 Topic 订阅成功，directFilter={}，gatewayFilter={}",
                                directFilter, gatewayFilter);
                        return;
                    }
                    log.error("服务响应 Topic 订阅失败，directFilter={}，gatewayFilter={}",
                            directFilter, gatewayFilter, throwable);
                });
    }

    /**
     * 订阅指定 Topic 过滤器。
     *
     * @param topicFilter Topic 过滤器
     * @return 订阅完成结果
     */
    private CompletableFuture<Void> subscribe(String topicFilter) {
        Mqtt5Subscribe subscribe = Mqtt5Subscribe.builder()
                .addSubscription()
                .topicFilter(topicFilter)
                .qos(resolveQos())
                .applySubscription()
                .build();
        return mqttClient.subscribe(subscribe, publish -> {
                    String topic = publish.getTopic().toString();
                    byte[] payload = publish.getPayloadAsBytes();
                    mqttUpstreamDispatcher.dispatch(topic, payload);
                })
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
