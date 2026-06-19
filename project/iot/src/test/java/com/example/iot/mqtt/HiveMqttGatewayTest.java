package com.example.iot.mqtt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.iot.config.MqttProperties;
import com.example.iot.model.MqttInvokeMessage;
import com.example.iot.service.MqttReplyHandler;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * {@link HiveMqttGateway} 的测试。
 */
class HiveMqttGatewayTest {

    private HiveMqttGateway gateway;

    private MqttProperties properties;

    @Mock
    private Mqtt5AsyncClient mqttClient;

    @Mock
    private MqttReplyHandler mqttReplyHandler;

    /**
     * 初始化测试依赖。
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        properties = new MqttProperties();
        gateway = new HiveMqttGateway(mqttClient, properties, mqttReplyHandler);
    }

    /**
     * 发布调用消息时应包含消息 ID 和业务数据。
     */
    @Test
    void shouldPublishInvokeMessageAsJson() {
        when(mqttClient.publish(any(Mqtt5Publish.class))).thenReturn(CompletableFuture.completedFuture(null));

        CompletableFuture<Void> future = gateway.sendInvoke(MqttInvokeMessage.builder()
                .msgId(124545L)
                .data("发的数据")
                .build());

        ArgumentCaptor<Mqtt5Publish> captor = ArgumentCaptor.forClass(Mqtt5Publish.class);
        verify(mqttClient).publish(captor.capture());
        Mqtt5Publish publish = captor.getValue();
        String payload = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
        assertThat(future).isCompleted();
        assertThat(publish.getTopic().toString()).hasToString(PseudoMqttGateway.INVOKE_TOPIC);
        assertThat(publish.getQos()).isEqualTo(MqttQos.AT_LEAST_ONCE);
        assertThat(payload).contains("\"msgId\":124545");
        assertThat(payload).contains("\"data\":\"发的数据\"");
    }

    /**
     * 收到合法 MQTT 回复时应完成对应服务调用。
     */
    @Test
    void shouldCompleteReplyWhenValidPayloadArrives() {
        gateway.handleReplyPayload("""
                {"msgId":124545,"data":"返回的数据"}
                """.getBytes(StandardCharsets.UTF_8));

        verify(mqttReplyHandler).completeReply(124545L, "返回的数据");
    }

    /**
     * 收到非法 JSON 时应忽略，不能把异常抛出到 MQTT 回调之外。
     */
    @Test
    void shouldIgnoreInvalidReplyPayload() {
        gateway.handleReplyPayload("不是 JSON".getBytes(StandardCharsets.UTF_8));

        verify(mqttReplyHandler, never()).completeReply(anyLong(), any());
    }

    /**
     * 收到缺少 msgId 的回复时应忽略。
     */
    @Test
    void shouldIgnoreReplyPayloadWithoutMsgId() {
        gateway.handleReplyPayload("""
                {"data":"返回的数据"}
                """.getBytes(StandardCharsets.UTF_8));

        verify(mqttReplyHandler, never()).completeReply(anyLong(), any());
    }
}
