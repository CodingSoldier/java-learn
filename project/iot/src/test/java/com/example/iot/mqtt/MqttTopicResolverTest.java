package com.example.iot.mqtt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.iot.model.MqttTopicMetadata;
import org.junit.jupiter.api.Test;

/**
 * {@link MqttTopicResolver} 的测试。
 */
class MqttTopicResolverTest {

    // ========== 直连设备 Topic 构建 ==========

    @Test
    void shouldBuildDirectServiceRequestTopic() {
        String topic = MqttTopicResolver.directServiceRequest("light", "light001", "switch");
        assertThat(topic).isEqualTo("iot/v1/products/light/devices/light001/down/services/switch/request");
    }

    @Test
    void shouldBuildDirectServiceResponseTopic() {
        String topic = MqttTopicResolver.directServiceResponse("light", "light001", "switch");
        assertThat(topic).isEqualTo("iot/v1/products/light/devices/light001/up/services/switch/response");
    }

    @Test
    void shouldReturnDirectServiceResponseFilter() {
        String filter = MqttTopicResolver.directServiceResponseFilter();
        assertThat(filter).isEqualTo("iot/v1/products/+/devices/+/up/services/+/response");
    }

    @Test
    void shouldBuildDirectDownFilter() {
        String filter = MqttTopicResolver.directDownFilter("light", "light001");
        assertThat(filter).isEqualTo("iot/v1/products/light/devices/light001/down/#");
    }

    // ========== 网关 Topic 构建 ==========

    @Test
    void shouldBuildGatewayServiceRequestTopic() {
        String topic = MqttTopicResolver.gatewayServiceRequest("gw001", "switch");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/down/sub-devices/services/switch/request");
    }

    @Test
    void shouldBuildGatewayServiceResponseTopic() {
        String topic = MqttTopicResolver.gatewayServiceResponse("gw001", "switch");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/up/sub-devices/services/switch/response");
    }

    @Test
    void shouldReturnGatewayServiceResponseFilter() {
        String filter = MqttTopicResolver.gatewayServiceResponseFilter();
        assertThat(filter).isEqualTo("iot/v1/gateways/+/up/sub-devices/services/+/response");
    }

    @Test
    void shouldBuildGatewayDownFilter() {
        String filter = MqttTopicResolver.gatewayDownFilter("gw001");
        assertThat(filter).isEqualTo("iot/v1/gateways/gw001/down/#");
    }

    // ========== 变量校验 ==========

    @Test
    void shouldRejectEmptyVariable() {
        assertThatThrownBy(() -> MqttTopicResolver.directServiceRequest("", "light001", "switch"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectVariableWithSlash() {
        assertThatThrownBy(() -> MqttTopicResolver.directServiceRequest("light/invalid", "light001", "switch"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectVariableWithPlus() {
        assertThatThrownBy(() -> MqttTopicResolver.directServiceRequest("light+invalid", "light001", "switch"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectVariableWithHash() {
        assertThatThrownBy(() -> MqttTopicResolver.directServiceRequest("light#invalid", "light001", "switch"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectVariableLongerThan64Chars() {
        String longVar = "a".repeat(65);
        assertThatThrownBy(() -> MqttTopicResolver.directServiceRequest(longVar, "light001", "switch"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldAcceptVariableWith64Chars() {
        String var64 = "a".repeat(64);
        String topic = MqttTopicResolver.directServiceRequest(var64, "light001", "switch");
        assertThat(topic).contains(var64);
    }

    // ========== 直连设备扩展 Topic 构建 ==========

    @Test
    void shouldBuildDirectPropertyReportTopic() {
        String topic = MqttTopicResolver.directPropertyReport("light", "light001");
        assertThat(topic).isEqualTo("iot/v1/products/light/devices/light001/up/properties/report");
    }

    @Test
    void shouldBuildDirectEventReportTopic() {
        String topic = MqttTopicResolver.directEventReport("smoke", "smoke001", "smoke-alarm");
        assertThat(topic).isEqualTo("iot/v1/products/smoke/devices/smoke001/up/events/smoke-alarm/report");
    }

    @Test
    void shouldBuildDirectPropertySetRequestTopic() {
        String topic = MqttTopicResolver.directPropertySetRequest("light", "light001");
        assertThat(topic).isEqualTo("iot/v1/products/light/devices/light001/down/properties/set/request");
    }

    @Test
    void shouldBuildDirectPropertySetResponseTopic() {
        String topic = MqttTopicResolver.directPropertySetResponse("light", "light001");
        assertThat(topic).isEqualTo("iot/v1/products/light/devices/light001/up/properties/set/response");
    }

    // ========== 网关扩展 Topic 构建 ==========

    @Test
    void shouldBuildGatewayPropertyReportTopic() {
        String topic = MqttTopicResolver.gatewayPropertyReport("gw001");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/up/sub-devices/properties/report");
    }

    @Test
    void shouldBuildGatewayEventReportTopic() {
        String topic = MqttTopicResolver.gatewayEventReport("gw001", "smoke-alarm");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/up/sub-devices/events/smoke-alarm/report");
    }

    @Test
    void shouldBuildGatewayStatusReportTopic() {
        String topic = MqttTopicResolver.gatewayStatusReport("gw001");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/up/sub-devices/status/report");
    }

    @Test
    void shouldBuildGatewayPropertySetRequestTopic() {
        String topic = MqttTopicResolver.gatewayPropertySetRequest("gw001");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/down/sub-devices/properties/set/request");
    }

    @Test
    void shouldBuildGatewayPropertySetResponseTopic() {
        String topic = MqttTopicResolver.gatewayPropertySetResponse("gw001");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/up/sub-devices/properties/set/response");
    }

    @Test
    void shouldBuildGatewayTopologySyncRequestTopic() {
        String topic = MqttTopicResolver.gatewayTopologySyncRequest("gw001");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/down/sub-devices/topology/sync/request");
    }

    @Test
    void shouldBuildGatewayTopologySyncResponseTopic() {
        String topic = MqttTopicResolver.gatewayTopologySyncResponse("gw001");
        assertThat(topic).isEqualTo("iot/v1/gateways/gw001/up/sub-devices/topology/sync/response");
    }

    // ========== Topic 解析 ==========

    @Test
    void shouldParseDirectServiceResponseTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/up/services/switch/response");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("DIRECT_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("up");
        assertThat(metadata.getProductKey()).isEqualTo("light");
        assertThat(metadata.getDeviceCode()).isEqualTo("light001");
        assertThat(metadata.getServiceCode()).isEqualTo("switch");
        assertThat(metadata.getMessageType()).isEqualTo("services-response");
    }

    @Test
    void shouldParseDirectServiceRequestTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/down/services/switch/request");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("DIRECT_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("down");
        assertThat(metadata.getMessageType()).isEqualTo("services-request");
    }

    @Test
    void shouldParseGatewayServiceResponseTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/up/sub-devices/services/switch/response");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("up");
        assertThat(metadata.getGatewayId()).isEqualTo("gw001");
        assertThat(metadata.getServiceCode()).isEqualTo("switch");
        assertThat(metadata.getMessageType()).isEqualTo("services-response");
    }

    @Test
    void shouldParseGatewayServiceRequestTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/down/sub-devices/services/switch/request");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("down");
        assertThat(metadata.getMessageType()).isEqualTo("services-request");
    }

    @Test
    void shouldReturnInvalidForNullTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(null);
        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldReturnInvalidForEmptyTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse("");
        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldReturnInvalidForWrongPrefix() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse("wrong/prefix/products/light/devices/light001/up/services/switch/response");
        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldReturnInvalidForInsufficientParts() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse("iot/v1/products");
        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldReturnInvalidForUnknownObjectType() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse("iot/v1/unknown/light/something");
        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldReturnInvalidForInvalidProductKey() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light+invalid/devices/light001/up/services/switch/response");
        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldParseDirectPropertiesReportTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/up/properties/report");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("DIRECT_DEVICE");
        assertThat(metadata.getMessageType()).isEqualTo("properties-report");
    }

    @Test
    void shouldParseDirectEventsReportTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/smoke/devices/smoke001/up/events/smoke-alarm/report");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("DIRECT_DEVICE");
        assertThat(metadata.getEventCode()).isEqualTo("smoke-alarm");
        assertThat(metadata.getMessageType()).isEqualTo("events-report");
    }

    @Test
    void shouldParseGatewayStatusReportTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/up/sub-devices/status/report");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getMessageType()).isEqualTo("status-report");
    }

    @Test
    void shouldParseGatewayTopologySyncResponseTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/up/sub-devices/topology/sync/response");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getMessageType()).isEqualTo("topology-sync-response");
    }

    @Test
    void shouldParseDirectPropertySetRequestTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/down/properties/set/request");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("DIRECT_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("down");
        assertThat(metadata.getMessageType()).isEqualTo("properties-set-request");
    }

    @Test
    void shouldParseDirectPropertySetResponseTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/up/properties/set/response");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("DIRECT_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("up");
        assertThat(metadata.getMessageType()).isEqualTo("properties-set-response");
    }

    @Test
    void shouldParseGatewayPropertyReportTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/up/sub-devices/properties/report");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getMessageType()).isEqualTo("properties-report");
    }

    @Test
    void shouldParseGatewayEventReportTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/up/sub-devices/events/smoke-alarm/report");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getEventCode()).isEqualTo("smoke-alarm");
        assertThat(metadata.getMessageType()).isEqualTo("events-report");
    }

    @Test
    void shouldParseGatewayPropertySetRequestTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/down/sub-devices/properties/set/request");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("down");
        assertThat(metadata.getMessageType()).isEqualTo("properties-set-request");
    }

    @Test
    void shouldParseGatewayPropertySetResponseTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/up/sub-devices/properties/set/response");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("up");
        assertThat(metadata.getMessageType()).isEqualTo("properties-set-response");
    }

    @Test
    void shouldParseGatewayTopologySyncRequestTopic() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/down/sub-devices/topology/sync/request");

        assertThat(metadata.isValid()).isTrue();
        assertThat(metadata.getAccessType()).isEqualTo("GATEWAY_SUB_DEVICE");
        assertThat(metadata.getDirection()).isEqualTo("down");
        assertThat(metadata.getMessageType()).isEqualTo("topology-sync-request");
    }

    @Test
    void shouldReturnEmptyStringForNonApplicableFields() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/up/sub-devices/services/switch/response");

        assertThat(metadata.getProductKey()).isEmpty();
        assertThat(metadata.getDeviceCode()).isEmpty();
        assertThat(metadata.getEventCode()).isEmpty();
    }

    @Test
    void shouldRejectTopicWithTrailingLevel() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/up/services/switch/response/extra");

        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldRejectTopicWithTrailingEmptyLevel() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/up/services/switch/response/");

        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldRejectTopicWithoutBusinessPath() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/up");

        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldRejectDirectResponseInDownDirection() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/down/services/switch/response");

        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldRejectGatewayRequestInUpDirection() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/gateways/gw001/up/sub-devices/services/switch/request");

        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldRejectUnknownBusinessType() {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(
                "iot/v1/products/light/devices/light001/up/unknown/report");

        assertThat(metadata.isValid()).isFalse();
    }

    @Test
    void shouldValidateTopicVarCorrectly() {
        assertThat(MqttTopicResolver.isValidTopicVar("light")).isTrue();
        assertThat(MqttTopicResolver.isValidTopicVar("light-001")).isTrue();
        assertThat(MqttTopicResolver.isValidTopicVar("light.001")).isTrue();
        assertThat(MqttTopicResolver.isValidTopicVar("light_001")).isTrue();
        assertThat(MqttTopicResolver.isValidTopicVar("light/001")).isFalse();
        assertThat(MqttTopicResolver.isValidTopicVar("light+001")).isFalse();
        assertThat(MqttTopicResolver.isValidTopicVar("light#001")).isFalse();
        assertThat(MqttTopicResolver.isValidTopicVar("light 001")).isFalse();
        assertThat(MqttTopicResolver.isValidTopicVar(null)).isFalse();
        assertThat(MqttTopicResolver.isValidTopicVar("")).isFalse();
    }
}
