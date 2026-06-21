package com.example.iot.mqtt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.iot.config.MqttProperties;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link HiveMqttUpstreamSubscriber} 的测试。
 */
@ExtendWith(MockitoExtension.class)
class HiveMqttUpstreamSubscriberTest {

    @Mock
    private Mqtt5AsyncClient mqttClient;

    @Mock
    private MqttUpstreamDispatcher mqttUpstreamDispatcher;

    @Test
    @SuppressWarnings("unchecked")
    void shouldSubscribeTwoServiceResponseFilters() {
        when(mqttClient.subscribe(any(Mqtt5Subscribe.class), any(Consumer.class)))
                .thenReturn(CompletableFuture.completedFuture((Mqtt5SubAck) null));

        HiveMqttUpstreamSubscriber subscriber = new HiveMqttUpstreamSubscriber(
                mqttClient, new MqttProperties(), mqttUpstreamDispatcher);

        CompletableFuture<Void> future = subscriber.subscribeServiceResponseTopics();

        assertThat(future).isCompleted();

        // 验证调用了两次 subscribe
        var subscribeCaptor = ArgumentCaptor.forClass(Mqtt5Subscribe.class);
        var callbackCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(mqttClient, org.mockito.Mockito.times(2))
                .subscribe(subscribeCaptor.capture(), callbackCaptor.capture());

        // 第一次：直连设备服务响应过滤器
        Mqtt5Subscribe firstSubscribe = subscribeCaptor.getAllValues().getFirst();
        assertThat(firstSubscribe.getSubscriptions()).hasSize(1);
        assertThat(firstSubscribe.getSubscriptions().getFirst().getTopicFilter().toString())
                .hasToString(MqttTopicResolver.directServiceResponseFilter());
        assertThat(firstSubscribe.getSubscriptions().getFirst().getQos()).isEqualTo(MqttQos.AT_LEAST_ONCE);

        // 第二次：网关子设备服务响应过滤器
        Mqtt5Subscribe secondSubscribe = subscribeCaptor.getAllValues().get(1);
        assertThat(secondSubscribe.getSubscriptions()).hasSize(1);
        assertThat(secondSubscribe.getSubscriptions().getFirst().getTopicFilter().toString())
                .hasToString(MqttTopicResolver.gatewayServiceResponseFilter());
        assertThat(secondSubscribe.getSubscriptions().getFirst().getQos()).isEqualTo(MqttQos.AT_LEAST_ONCE);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldDispatchPayloadToUpstreamDispatcher() {
        when(mqttClient.subscribe(any(Mqtt5Subscribe.class), any(Consumer.class)))
                .thenReturn(CompletableFuture.completedFuture((Mqtt5SubAck) null));

        HiveMqttUpstreamSubscriber subscriber = new HiveMqttUpstreamSubscriber(
                mqttClient, new MqttProperties(), mqttUpstreamDispatcher);

        subscriber.subscribeServiceResponseTopics();

        var callbackCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(mqttClient, org.mockito.Mockito.times(2))
                .subscribe(any(Mqtt5Subscribe.class), callbackCaptor.capture());

        // 模拟收到消息
        byte[] payload = """
                {"msgId":"124545","code":20000,"message":"成功"}
                """.getBytes(StandardCharsets.UTF_8);
        Mqtt5Publish publish = Mqtt5Publish.builder()
                .topic("iot/v1/products/light/devices/light001/up/services/switch/response")
                .payload(payload)
                .build();

        callbackCaptor.getAllValues().getFirst().accept(publish);

        verify(mqttUpstreamDispatcher).dispatch(
                "iot/v1/products/light/devices/light001/up/services/switch/response",
                payload);
    }
}
