package com.example.iot.mqtt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.iot.config.MqttProperties;
import com.example.iot.model.MqttPublishRequest;
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
     * 发布请求消息时应包含正确的 topic、payload 和 MQTT 5 属性。
     */
    @Test
    void shouldPublishRequestWithCorrectTopicAndPayload() {
        when(mqttClient.publish(any(Mqtt5Publish.class))).thenReturn(CompletableFuture.completedFuture(null));

        byte[] payload = """
                {"msgId":"124545","timestamp":1781883000000,"data":{"value":true}}
                """.getBytes(StandardCharsets.UTF_8);
        byte[] correlationData = "124545".getBytes(StandardCharsets.UTF_8);

        MqttPublishRequest request = MqttPublishRequest.builder()
                .topic("sys/v1/products/light/devices/light001/down/services/switch/request")
                .payload(payload)
                .correlationData(correlationData)
                .build();

        CompletableFuture<Void> future = gateway.publish(request);

        ArgumentCaptor<Mqtt5Publish> captor = ArgumentCaptor.forClass(Mqtt5Publish.class);
        verify(mqttClient).publish(captor.capture());
        Mqtt5Publish publish = captor.getValue();
        String publishedPayload = new String(publish.getPayloadAsBytes(), StandardCharsets.UTF_8);
        assertThat(future).isCompleted();
        assertThat(publish.getTopic().toString())
                .hasToString("sys/v1/products/light/devices/light001/down/services/switch/request");
        assertThat(publish.getQos()).isEqualTo(MqttQos.AT_LEAST_ONCE);
        assertThat(publishedPayload).contains("\"msgId\":\"124545\"");
        assertThat(publishedPayload).contains("\"value\":true");
        assertThat(publish.getCorrelationData()).isPresent();
        java.nio.ByteBuffer corrData = publish.getCorrelationData().get();
        byte[] corrBytes = new byte[corrData.remaining()];
        corrData.get(corrBytes);
        corrData.rewind();
        assertThat(new String(corrBytes, StandardCharsets.UTF_8))
                .isEqualTo("124545");
    }

    /**
     * 发布简单 payload 到指定主题。
     */
    @Test
    void shouldPublishSimplePayload() {
        when(mqttClient.publish(any(Mqtt5Publish.class))).thenReturn(CompletableFuture.completedFuture(null));

        byte[] payload = "{\"test\":true}".getBytes(StandardCharsets.UTF_8);
        CompletableFuture<Void> future = gateway.publish("test/topic", payload);

        ArgumentCaptor<Mqtt5Publish> captor = ArgumentCaptor.forClass(Mqtt5Publish.class);
        verify(mqttClient).publish(captor.capture());
        assertThat(future).isCompleted();
        assertThat(captor.getValue().getTopic().toString()).hasToString("test/topic");
    }
}
