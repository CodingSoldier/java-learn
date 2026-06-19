package com.example.iot.mqtt;

import com.example.iot.config.MqttProperties;
import com.example.iot.model.MqttInvokeMessage;
import com.example.iot.model.MqttReplyMessage;
import com.example.iot.service.MqttReplyHandler;
import com.github.codingsoldier.common.util.objectmapper.ObjectMapperUtil;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import jakarta.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 基于 HiveMQ Client 的真实 MQTT 网关。
 */
@Slf4j
@Service
public class HiveMqttGateway implements MqttGateway {

    private final Mqtt5AsyncClient mqttClient;

    private final MqttProperties properties;

    private final MqttReplyHandler mqttReplyHandler;

    /**
     * 创建真实 MQTT 网关。
     *
     * @param mqttClient MQTT 5 异步客户端
     * @param properties MQTT 配置
     * @param mqttReplyHandler MQTT 回复处理器
     */
    @Autowired
    public HiveMqttGateway(Mqtt5AsyncClient mqttClient, MqttProperties properties, MqttReplyHandler mqttReplyHandler) {
        this.mqttClient = mqttClient;
        this.properties = properties;
        this.mqttReplyHandler = mqttReplyHandler;
    }

    /**
     * 应用启动完成后连接 MQTT 并订阅回复主题。
     */
    @EventListener(ApplicationReadyEvent.class)
    public void connectAndSubscribe() {
        mqttClient.connect(buildConnectMessage())
                .orTimeout(properties.getConnectTimeout().toMillis(), TimeUnit.MILLISECONDS)
                .thenCompose(ignored -> subscribeReplyTopic())
                .whenComplete((ignored, throwable) -> {
                    if (throwable == null) {
                        log.info("MQTT 客户端连接并订阅成功，host={}，port={}，clientId={}，replyTopic={}",
                                properties.getHost(), properties.getPort(), properties.getClientId(),
                                PseudoMqttGateway.INVOKE_REPLY_TOPIC);
                        return;
                    }
                    log.error("MQTT 客户端连接或订阅失败，host={}，port={}，clientId={}，replyTopic={}",
                            properties.getHost(), properties.getPort(), properties.getClientId(),
                            PseudoMqttGateway.INVOKE_REPLY_TOPIC, throwable);
                });
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
                    .topic(PseudoMqttGateway.INVOKE_TOPIC)
                    .qos(resolveQos())
                    .payload(payload)
                    .build();
            return mqttClient.publish(publish)
                    .thenApply(ignored -> (Void) null)
                    .whenComplete((ignored, throwable) -> {
                        if (throwable == null) {
                            log.info("MQTT 发布成功，topic={}，msgId={}",
                                    PseudoMqttGateway.INVOKE_TOPIC, message.getMsgId());
                            return;
                        }
                        log.error("MQTT 发布失败，topic={}，msgId={}",
                                PseudoMqttGateway.INVOKE_TOPIC, message.getMsgId(), throwable);
                    });
        } catch (RuntimeException ex) {
            log.error("序列化 MQTT 调用消息失败，msgId={}", message.getMsgId(), ex);
            return CompletableFuture.<Void>failedFuture(new IllegalStateException("序列化 MQTT 调用消息失败", ex));
        }
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

    void handleReplyPayload(byte[] payload) {
        MqttReplyMessage message = ObjectMapperUtil.readValue(payload, MqttReplyMessage.class);
        if (message == null) {
            log.error("解析 MQTT 回复失败，topic={}，payload={}",
                    PseudoMqttGateway.INVOKE_REPLY_TOPIC, new String(payload, StandardCharsets.UTF_8));
            return;
        }
        if (message.getMsgId() == null) {
            log.warn("MQTT 回复缺少 msgId，topic={}，payload={}",
                    PseudoMqttGateway.INVOKE_REPLY_TOPIC, new String(payload, StandardCharsets.UTF_8));
            return;
        }
        boolean matched = mqttReplyHandler.completeReply(message.getMsgId(), message.getData());
        log.info("MQTT 回复处理完成，topic={}，msgId={}，matched={}",
                PseudoMqttGateway.INVOKE_REPLY_TOPIC, message.getMsgId(), matched);
    }

    private CompletableFuture<Void> subscribeReplyTopic() {
        Mqtt5Subscribe subscribe = Mqtt5Subscribe.builder()
                .addSubscription()
                .topicFilter(PseudoMqttGateway.INVOKE_REPLY_TOPIC)
                .qos(resolveQos())
                .applySubscription()
                .build();
        return mqttClient.subscribe(subscribe, publish -> handleReplyPayload(publish.getPayloadAsBytes()))
                .thenApply(ignored -> null);
    }

    private Mqtt5Connect buildConnectMessage() {
        var builder = Mqtt5Connect.builder()
                .keepAlive((int) properties.getKeepAlive().toSeconds())
                .cleanStart(true);
        if (StringUtils.hasText(properties.getUsername())) {
            builder.simpleAuth(buildSimpleAuth());
        }
        return builder.build();
    }

    private Mqtt5SimpleAuth buildSimpleAuth() {
        var builder = Mqtt5SimpleAuth.builder()
                .username(properties.getUsername());
        if (StringUtils.hasLength(properties.getPassword())) {
            builder.password(properties.getPassword().getBytes(StandardCharsets.UTF_8));
        }
        return builder.build();
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
