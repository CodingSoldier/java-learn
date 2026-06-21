package com.example.iot.mqtt;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.iot.model.MqttTopicMetadata;
import com.example.iot.model.ServiceResponseMessage;
import com.example.iot.service.PendingRequestRegistry;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * {@link ServiceResponseHandler} 的测试。
 */
class ServiceResponseHandlerTest {

    private PendingRequestRegistry registry;
    private ServiceResponseHandler handler;

    @BeforeEach
    void setUp() {
        registry = new PendingRequestRegistry(100);
        handler = new ServiceResponseHandler(registry);
    }

    @AfterEach
    void tearDown() {
        registry.shutdown();
    }

    @Test
    void shouldCompleteDirectDeviceServiceResponse() {
        registry.register("124545", Duration.ofSeconds(5), "light", "light001", null, "switch");

        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("DIRECT_DEVICE")
                .direction("up")
                .productKey("light")
                .deviceCode("light001")
                .serviceCode("switch")
                .messageType("services-response")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .msgId("124545")
                .code(20000)
                .message("成功")
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isTrue();
        assertThat(registry.size()).isZero();
    }

    @Test
    void shouldCompleteGatewayServiceResponse() {
        registry.register("124549", Duration.ofSeconds(5), "sensor", "sensor001", "gw001", "switch");

        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("GATEWAY_SUB_DEVICE")
                .direction("up")
                .gatewayId("gw001")
                .serviceCode("switch")
                .messageType("services-response")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .msgId("124549")
                .code(20000)
                .message("成功")
                .target(ServiceResponseMessage.TargetInfo.builder()
                        .productKey("sensor")
                        .deviceCode("sensor001")
                        .build())
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isTrue();
        assertThat(registry.size()).isZero();
    }

    @Test
    void shouldReturnFalseForUnknownMsgId() {
        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("DIRECT_DEVICE")
                .productKey("light")
                .deviceCode("light001")
                .serviceCode("switch")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .msgId("999999")
                .code(20000)
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isFalse();
    }

    @Test
    void shouldReturnFalseWhenMsgIdMissing() {
        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("DIRECT_DEVICE")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .code(20000)
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isFalse();
    }

    @Test
    void shouldReturnFalseWhenCodeMissing() {
        registry.register("123", Duration.ofSeconds(5), "light", "light001", null, "switch");

        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("DIRECT_DEVICE")
                .productKey("light")
                .deviceCode("light001")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .msgId("123")
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isFalse();
    }

    @Test
    void shouldRejectDirectDeviceResponseWithMismatchedIdentity() {
        registry.register("123", Duration.ofSeconds(5), "light", "light001", null, "switch");

        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("DIRECT_DEVICE")
                .productKey("light")
                .deviceCode("wrong-device")
                .serviceCode("switch")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .msgId("123")
                .code(20000)
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isFalse();
    }

    @Test
    void shouldRejectGatewayResponseWithMismatchedGatewayId() {
        registry.register("123", Duration.ofSeconds(5), "sensor", "sensor001", "gw001", "switch");

        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("GATEWAY_SUB_DEVICE")
                .gatewayId("wrong-gw")
                .serviceCode("switch")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .msgId("123")
                .code(20000)
                .target(ServiceResponseMessage.TargetInfo.builder()
                        .productKey("sensor")
                        .deviceCode("sensor001")
                        .build())
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isFalse();
    }

    @Test
    void shouldRejectGatewayResponseWithMismatchedTarget() {
        registry.register("123", Duration.ofSeconds(5), "sensor", "sensor001", "gw001", "switch");

        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("GATEWAY_SUB_DEVICE")
                .gatewayId("gw001")
                .serviceCode("switch")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .msgId("123")
                .code(20000)
                .target(ServiceResponseMessage.TargetInfo.builder()
                        .productKey("sensor")
                        .deviceCode("wrong-device")
                        .build())
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isFalse();
    }

    @Test
    void shouldCompletePendingRequestWithFullNonSuccessResponse() throws Exception {
        var pendingRequest = registry.register("123", Duration.ofSeconds(5),
                "light", "light001", null, "switch");

        MqttTopicMetadata metadata = MqttTopicMetadata.builder()
                .valid(true)
                .accessType("DIRECT_DEVICE")
                .productKey("light")
                .deviceCode("light001")
                .serviceCode("switch")
                .build();

        ServiceResponseMessage message = ServiceResponseMessage.builder()
                .msgId("123")
                .code(50000)
                .message("设备内部错误")
                .data(Map.of("detail", "设备过载"))
                .build();

        boolean matched = handler.handle(metadata, message);
        assertThat(matched).isTrue();
        assertThat(pendingRequest.getFuture().get(1, TimeUnit.SECONDS)).isSameAs(message);
        assertThat(registry.size()).isZero();
    }
}
