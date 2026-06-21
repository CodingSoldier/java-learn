package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT Topic 解析元数据，包含从 Topic 中提取的路由和业务信息。
 * 不适用的字段使用空字符串，不返回 null。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MqttTopicMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 接入类型：DIRECT_DEVICE 或 GATEWAY_SUB_DEVICE。
     */
    private String accessType = "";

    /**
     * 消息方向：UP 或 DOWN。
     */
    private String direction = "";

    /**
     * 消息类型：services-response、properties-report 等。
     */
    private String messageType = "";

    /**
     * 产品标识，仅直连设备有效。
     */
    private String productKey = "";

    /**
     * 设备编码，仅直连设备有效。
     */
    private String deviceCode = "";

    /**
     * 网关标识，仅网关子设备有效。
     */
    private String gatewayId = "";

    /**
     * 服务编码，仅服务调用消息有效。
     */
    private String serviceCode = "";

    /**
     * 事件编码，仅事件上报消息有效。
     */
    private String eventCode = "";

    /**
     * Topic 解析是否成功。
     */
    private boolean valid;

    /**
     * 创建构建器。
     *
     * @return 新的构建器实例
     */
    public static MqttTopicMetadataBuilder builder() {
        return new MqttTopicMetadataBuilder();
    }

    /**
     * MqttTopicMetadata 构建器。
     */
    public static class MqttTopicMetadataBuilder {
        private final MqttTopicMetadata instance = new MqttTopicMetadata();

        public MqttTopicMetadataBuilder valid(boolean valid) {
            instance.setValid(valid);
            return this;
        }

        public MqttTopicMetadataBuilder accessType(String accessType) {
            instance.setAccessType(accessType);
            return this;
        }

        public MqttTopicMetadataBuilder direction(String direction) {
            instance.setDirection(direction);
            return this;
        }

        public MqttTopicMetadataBuilder messageType(String messageType) {
            instance.setMessageType(messageType);
            return this;
        }

        public MqttTopicMetadataBuilder productKey(String productKey) {
            instance.setProductKey(productKey);
            return this;
        }

        public MqttTopicMetadataBuilder deviceCode(String deviceCode) {
            instance.setDeviceCode(deviceCode);
            return this;
        }

        public MqttTopicMetadataBuilder gatewayId(String gatewayId) {
            instance.setGatewayId(gatewayId);
            return this;
        }

        public MqttTopicMetadataBuilder serviceCode(String serviceCode) {
            instance.setServiceCode(serviceCode);
            return this;
        }

        public MqttTopicMetadataBuilder eventCode(String eventCode) {
            instance.setEventCode(eventCode);
            return this;
        }

        public MqttTopicMetadata build() {
            return instance;
        }
    }
}
