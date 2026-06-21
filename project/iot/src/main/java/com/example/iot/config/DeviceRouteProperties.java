package com.example.iot.config;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 设备路由配置属性。
 */
@Data
@Component
@ConfigurationProperties(prefix = "iot.routing")
public class DeviceRouteProperties {

    /**
     * 设备路由列表。
     */
    private List<DeviceRouteConfig> devices = new ArrayList<>();

    /**
     * 单台设备的路由配置。
     */
    @Data
    public static class DeviceRouteConfig {

        /**
         * 产品标识。
         */
        private String productKey;

        /**
         * 设备编码。
         */
        private String deviceCode;

        /**
         * 接入类型：DIRECT_DEVICE 或 GATEWAY_SUB_DEVICE。
         */
        private String accessType;

        /**
         * 网关标识，仅 GATEWAY_SUB_DEVICE 有效。
         */
        private String gatewayId;
    }
}
