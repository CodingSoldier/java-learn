package com.example.iot.mqtt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.iot.model.MqttDispatchResult;
import com.example.iot.model.MqttTopicMetadata;
import com.example.iot.model.ServiceResponseMessage;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * {@link MqttUpstreamDispatcher} 的测试。
 */
@ExtendWith(MockitoExtension.class)
class MqttUpstreamDispatcherTest {

    @Mock
    private ServiceResponseHandler serviceResponseHandler;

    @InjectMocks
    private MqttUpstreamDispatcher dispatcher;

    @Test
    void shouldDispatchDirectServiceResponse() {
        when(serviceResponseHandler.handle(any(), any())).thenReturn(true);

        String payload = validPayload("124545", "20000", "{\"value\":true}");
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                payload.getBytes(StandardCharsets.UTF_8));

        assertThat(result.getMsgId()).isEqualTo("124545");
        assertThat(result.isMatched()).isTrue();

        ArgumentCaptor<MqttTopicMetadata> metadataCaptor = ArgumentCaptor.forClass(MqttTopicMetadata.class);
        ArgumentCaptor<ServiceResponseMessage> messageCaptor = ArgumentCaptor.forClass(ServiceResponseMessage.class);
        verify(serviceResponseHandler).handle(metadataCaptor.capture(), messageCaptor.capture());

        MqttTopicMetadata metadata = metadataCaptor.getValue();
        assertThat(metadata.getAccessType()).isEqualTo("DIRECT_DEVICE");
        assertThat(metadata.getProductKey()).isEqualTo("light");
        assertThat(metadata.getDeviceCode()).isEqualTo("light001");
        assertThat(metadata.getServiceCode()).isEqualTo("switch");

        ServiceResponseMessage message = messageCaptor.getValue();
        assertThat(message.getMsgId()).isEqualTo("124545");
        assertThat(message.getCode()).isEqualTo(20000);
        assertThat(message.getMessage()).isEqualTo("成功");
    }

    @Test
    void shouldDispatchGatewayServiceResponse() {
        when(serviceResponseHandler.handle(any(), any())).thenReturn(true);

        String payload = "{\"msgId\":\"124549\",\"timestamp\":" + System.currentTimeMillis()
                + ",\"code\":20000,\"message\":\"成功\","
                + "\"target\":{\"productKey\":\"sensor\",\"deviceCode\":\"sensor001\"},"
                + "\"data\":{\"value\":true}}";
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/gateways/gw001/up/sub-devices/services/switch/response",
                payload.getBytes(StandardCharsets.UTF_8));

        assertThat(result.getMsgId()).isEqualTo("124549");
        assertThat(result.isMatched()).isTrue();

        ArgumentCaptor<ServiceResponseMessage> messageCaptor = ArgumentCaptor.forClass(ServiceResponseMessage.class);
        verify(serviceResponseHandler).handle(any(), messageCaptor.capture());

        ServiceResponseMessage message = messageCaptor.getValue();
        assertThat(message.getTarget()).isNotNull();
        assertThat(message.getTarget().getProductKey()).isEqualTo("sensor");
        assertThat(message.getTarget().getDeviceCode()).isEqualTo("sensor001");
    }

    @Test
    void shouldReturnMatchedFalseForInvalidTopic() {
        MqttDispatchResult result = dispatcher.dispatch("invalid/topic", "{}".getBytes(StandardCharsets.UTF_8));
        assertThat(result.isMatched()).isFalse();
    }

    @Test
    void shouldReturnMatchedFalseForInvalidJson() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                "不是 JSON".getBytes(StandardCharsets.UTF_8));
        assertThat(result.isMatched()).isFalse();
    }

    @Test
    void shouldReturnMatchedFalseWhenMsgIdMissing() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                "{\"code\":20000}".getBytes(StandardCharsets.UTF_8));
        assertThat(result.getMsgId()).isEmpty();
        assertThat(result.isMatched()).isFalse();
        verifyNoInteractions(serviceResponseHandler);
    }

    @Test
    void shouldReturnMatchedFalseWhenCodeMissing() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                "{\"msgId\":\"123\"}".getBytes(StandardCharsets.UTF_8));
        assertThat(result.getMsgId()).isEqualTo("123");
        assertThat(result.isMatched()).isFalse();
        verifyNoInteractions(serviceResponseHandler);
    }

    @Test
    void shouldReturnMatchedFalseForGatewayResponseWithoutTarget() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/gateways/gw001/up/sub-devices/services/switch/response",
                "{\"msgId\":\"123\",\"code\":20000}".getBytes(StandardCharsets.UTF_8));
        assertThat(result.getMsgId()).isEqualTo("123");
        assertThat(result.isMatched()).isFalse();
        verifyNoInteractions(serviceResponseHandler);
    }

    @Test
    void shouldReturnMatchedFalseForUnsupportedMessageType() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/properties/report",
                "{\"msgId\":\"123\"}".getBytes(StandardCharsets.UTF_8));
        assertThat(result.isMatched()).isFalse();
    }

    @Test
    void shouldRejectNumericMsgId() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                validPayload("123", "20000", "{}")
                        .replace("\"123\"", "123")
                        .getBytes(StandardCharsets.UTF_8));

        assertThat(result.getMsgId()).isEmpty();
        assertThat(result.isMatched()).isFalse();
    }

    @Test
    void shouldRejectMissingTimestamp() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                "{\"msgId\":\"123\",\"code\":20000,\"data\":{}}".getBytes(StandardCharsets.UTF_8));

        assertThat(result.getMsgId()).isEqualTo("123");
        assertThat(result.isMatched()).isFalse();
        verifyNoInteractions(serviceResponseHandler);
    }

    @Test
    void shouldRejectStringCode() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                validPayload("123", "\"20000\"", "{}").getBytes(StandardCharsets.UTF_8));

        assertThat(result.getMsgId()).isEqualTo("123");
        assertThat(result.isMatched()).isFalse();
        verifyNoInteractions(serviceResponseHandler);
    }

    @Test
    void shouldRejectNonObjectData() {
        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                validPayload("123", "20000", "\"错误类型\"").getBytes(StandardCharsets.UTF_8));

        assertThat(result.getMsgId()).isEqualTo("123");
        assertThat(result.isMatched()).isFalse();
    }

    @Test
    void shouldNormalizeMissingDataToEmptyMap() {
        when(serviceResponseHandler.handle(any(), any())).thenReturn(true);
        String payload = "{\"msgId\":\"123\",\"timestamp\":" + System.currentTimeMillis()
                + ",\"code\":20000,\"message\":\"成功\"}";

        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/products/light/devices/light001/up/services/switch/response",
                payload.getBytes(StandardCharsets.UTF_8));

        ArgumentCaptor<ServiceResponseMessage> messageCaptor = ArgumentCaptor.forClass(ServiceResponseMessage.class);
        verify(serviceResponseHandler).handle(any(), messageCaptor.capture());
        assertThat(result.isMatched()).isTrue();
        assertThat(messageCaptor.getValue().getData()).isEmpty();
    }

    @Test
    void shouldRejectBlankGatewayTargetIdentity() {
        String payload = "{\"msgId\":\"123\",\"timestamp\":" + System.currentTimeMillis()
                + ",\"code\":20000,\"target\":{\"productKey\":\"\",\"deviceCode\":\"sensor001\"},\"data\":{}}";

        MqttDispatchResult result = dispatcher.dispatch(
                "sys/v1/gateways/gw001/up/sub-devices/services/switch/response",
                payload.getBytes(StandardCharsets.UTF_8));

        assertThat(result.getMsgId()).isEqualTo("123");
        assertThat(result.isMatched()).isFalse();
    }

    private String validPayload(String msgId, String code, String data) {
        return "{\"msgId\":\"" + msgId + "\",\"timestamp\":" + System.currentTimeMillis()
                + ",\"code\":" + code + ",\"message\":\"成功\",\"data\":" + data + "}";
    }
}
