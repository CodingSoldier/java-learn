package com.example.iot.mqtt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.iot.config.MqttProperties;
import com.example.iot.model.MqttInvokeMessage;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link HiveMqttGateway} 的测试。
 */
@ExtendWith(MockitoExtension.class)
class HiveMqttGatewayTest {

    private HiveMqttGateway gateway;

    @Mock
    private Mqtt5AsyncClient mqttClient;

    /**
     * 初始化测试依赖。
     */
    @BeforeEach
    void setUp() {
        MqttProperties properties = new MqttProperties();
        gateway = new HiveMqttGateway(mqttClient, properties);
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
        assertThat(publish.getTopic().toString()).hasToString(MqttTopics.INVOKE_TOPIC);
        assertThat(publish.getQos()).isEqualTo(MqttQos.AT_LEAST_ONCE);
        assertThat(payload).contains("\"msgId\":124545");
        assertThat(payload).contains("\"data\":\"发的数据\"");
    }
}
