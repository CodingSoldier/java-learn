package com.example.iot.mqtt;

import com.example.iot.config.MqttProperties;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import jakarta.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * HiveMQ MQTT 客户端生命周期管理器。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HiveMqttClientLifecycle {

    private final Mqtt5AsyncClient mqttClient;

    private final MqttProperties properties;

    private final HiveMqttReplySubscriber hiveMqttReplySubscriber;

    /**
     * 应用启动完成后连接 MQTT 并订阅回复主题。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void connectAndSubscribe() {
        mqttClient.connect(buildConnectMessage())
                .orTimeout(properties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .thenCompose(ignored -> hiveMqttReplySubscriber.subscribeReplyTopic())
                .whenComplete((ignored, throwable) -> {
                    if (throwable == null) {
                        log.info("MQTT 客户端连接并订阅成功，host={}，port={}，clientId={}，replyTopic={}",
                                properties.getHost(), properties.getPort(), properties.getClientId(),
                                MqttTopics.INVOKE_REPLY_TOPIC);
                        return;
                    }
                    log.error("MQTT 客户端连接或订阅失败，host={}，port={}，clientId={}，replyTopic={}",
                            properties.getHost(), properties.getPort(), properties.getClientId(),
                            MqttTopics.INVOKE_REPLY_TOPIC, throwable);
                });
    }

    /**
     * 关闭 MQTT 客户端连接。
     */
    @PreDestroy
    public void disconnect() {
        mqttClient.disconnect().whenComplete((ignored, throwable) -> {
            if (throwable == null) {
                log.info("MQTT 客户端已断开连接，clientId={}", properties.getClientId());
                return;
            }
            log.warn("MQTT 客户端断开连接失败，clientId={}", properties.getClientId(), throwable);
        });
    }

    private Mqtt5Connect buildConnectMessage() {
        var builder = Mqtt5Connect.builder()
                .keepAlive((int) properties.getKeepAlive().toSeconds())
                .cleanStart(true);
        if (StringUtils.hasText(properties.getUsername())) {
            builder = builder.simpleAuth(buildSimpleAuth());
        }
        return builder.build();
    }

    private Mqtt5SimpleAuth buildSimpleAuth() {
        var builder = Mqtt5SimpleAuth.builder()
                .username(properties.getUsername());
        if (StringUtils.hasLength(properties.getPassword())) {
            builder = builder.password(properties.getPassword().getBytes(StandardCharsets.UTF_8));
        }
        return builder.build();
    }
}
