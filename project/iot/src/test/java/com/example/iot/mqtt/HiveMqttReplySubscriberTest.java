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
 * {@link HiveMqttReplySubscriber} 的测试。
 */
@ExtendWith(MockitoExtension.class)
class HiveMqttReplySubscriberTest {

    @Mock
    private Mqtt5AsyncClient mqttClient;

    @Mock
    private MqttReplyPayloadHandler mqttReplyPayloadHandler;

    /**
     * 订阅回复主题并把收到的载荷委托给处理器。
     */
    @Test
    @SuppressWarnings("unchecked")
    void shouldSubscribeReplyTopicAndDispatchPayload() {
        when(mqttClient.subscribe(any(Mqtt5Subscribe.class), any(Consumer.class)))
                .thenReturn(CompletableFuture.completedFuture((Mqtt5SubAck) null));
        HiveMqttReplySubscriber subscriber = new HiveMqttReplySubscriber(
                mqttClient, new MqttProperties(), mqttReplyPayloadHandler);

        CompletableFuture<Void> future = subscriber.subscribeReplyTopic();

        ArgumentCaptor<Mqtt5Subscribe> subscribeCaptor = ArgumentCaptor.forClass(Mqtt5Subscribe.class);
        ArgumentCaptor<Consumer<Mqtt5Publish>> callbackCaptor = ArgumentCaptor.forClass(Consumer.class);
        verify(mqttClient).subscribe(subscribeCaptor.capture(), callbackCaptor.capture());
        Mqtt5Subscribe subscribe = subscribeCaptor.getValue();
        assertThat(future).isCompleted();
        assertThat(subscribe.getSubscriptions()).hasSize(1);
        assertThat(subscribe.getSubscriptions().getFirst().getTopicFilter().toString())
                .hasToString(MqttTopics.INVOKE_REPLY_TOPIC);
        assertThat(subscribe.getSubscriptions().getFirst().getQos()).isEqualTo(MqttQos.AT_LEAST_ONCE);

        byte[] payload = """
                {"msgId":124545,"data":"返回的数据"}
                """.getBytes(StandardCharsets.UTF_8);
        Mqtt5Publish publish = Mqtt5Publish.builder()
                .topic(MqttTopics.INVOKE_REPLY_TOPIC)
                .payload(payload)
                .build();
        callbackCaptor.getValue().accept(publish);

        verify(mqttReplyPayloadHandler).handle(payload);
    }
}
