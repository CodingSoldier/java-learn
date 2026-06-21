package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT 服务调用响应消息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponseMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息 ID，与请求 msgId 对应。
     */
    private String msgId;

    /**
     * 消息发送时间，UTC Unix 毫秒时间戳。
     */
    private Long timestamp;

    /**
     * 业务结果码。
     */
    private Integer code;

    /**
     * 结果说明；成功时为"成功"，失败时提供可定位原因的中文描述。
     */
    private String message;

    /**
     * 服务调用返回的数据，JSON 对象类型；缺省时归一化为空 Map。
     */
    @Builder.Default
    private Map<String, Object> data = new HashMap<>();

    /**
     * 目标子设备标识，仅网关代理消息有效；直连设备为 null。
     */
    private TargetInfo target;

    /**
     * 目标子设备标识，用于网关代理消息。
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TargetInfo implements Serializable {

        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * 子设备产品标识。
         */
        private String productKey;

        /**
         * 子设备编码。
         */
        private String deviceCode;
    }
}
