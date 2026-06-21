package com.example.iot.routing;

import com.example.iot.config.DeviceRouteProperties;
import com.example.iot.config.DeviceRouteProperties.DeviceRouteConfig;
import com.github.codingsoldier.common.exception.HttpStatus4xxException;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * 基于内存配置的设备路由解析器。
 * <p>
 * 启动时从 {@link DeviceRouteProperties} 加载设备路由配置，构建只读路由索引。
 * 重复设备配置会立即终止启动。
 */
@Slf4j
@Component
public class InMemoryDeviceRouteResolver implements DeviceRouteResolver {

    private final DeviceRouteProperties properties;

    private Map<String, DeviceRoute> routeIndex;

    /**
     * 使用设备路由配置创建解析器。
     *
     * @param properties 设备路由配置
     */
    public InMemoryDeviceRouteResolver(DeviceRouteProperties properties) {
        this.properties = properties;
    }

    /**
     * 初始化路由索引，校验配置合法性。
     */
    @PostConstruct
    public void init() {
        List<DeviceRouteConfig> devices = properties.getDevices();
        Map<String, DeviceRoute> index = new HashMap<>();

        if (devices == null || devices.isEmpty()) {
            log.info("未配置设备路由");
            routeIndex = index;
            return;
        }

        for (DeviceRouteConfig config : devices) {
            String productKey = config.getProductKey();
            String deviceCode = config.getDeviceCode();
            String accessTypeStr = config.getAccessType();
            String gatewayId = config.getGatewayId();

            if (!StringUtils.hasText(productKey) || !StringUtils.hasText(deviceCode)) {
                log.error("设备路由配置缺少 productKey 或 deviceCode，config={}", config);
                throw new IllegalStateException("设备路由配置缺少 productKey 或 deviceCode");
            }

            if (!StringUtils.hasText(accessTypeStr)) {
                log.error("设备路由配置缺少 accessType，productKey={}，deviceCode={}", productKey, deviceCode);
                throw new IllegalStateException("设备路由配置缺少 accessType，productKey=" + productKey + "，deviceCode=" + deviceCode);
            }

            AccessType accessType;
            try {
                accessType = AccessType.valueOf(accessTypeStr);
            } catch (IllegalArgumentException e) {
                log.error("设备路由配置 accessType 不合法，accessType={}，productKey={}，deviceCode={}",
                        accessTypeStr, productKey, deviceCode);
                throw new IllegalStateException("设备路由配置 accessType 不合法：" + accessTypeStr, e);
            }

            if (accessType == AccessType.DIRECT_DEVICE && StringUtils.hasText(gatewayId)) {
                log.error("直连设备不允许配置 gatewayId，productKey={}，deviceCode={}", productKey, deviceCode);
                throw new IllegalStateException("直连设备不允许配置 gatewayId，productKey=" + productKey + "，deviceCode=" + deviceCode);
            }

            if (accessType == AccessType.GATEWAY_SUB_DEVICE && !StringUtils.hasText(gatewayId)) {
                log.error("网关子设备必须配置 gatewayId，productKey={}，deviceCode={}", productKey, deviceCode);
                throw new IllegalStateException("网关子设备必须配置 gatewayId，productKey=" + productKey + "，deviceCode=" + deviceCode);
            }

            String key = buildKey(productKey, deviceCode);
            if (index.containsKey(key)) {
                log.error("设备路由配置重复，productKey={}，deviceCode={}", productKey, deviceCode);
                throw new IllegalStateException("设备路由配置重复，productKey=" + productKey + "，deviceCode=" + deviceCode);
            }

            DeviceRoute route = DeviceRoute.builder()
                    .accessType(accessType)
                    .productKey(productKey)
                    .deviceCode(deviceCode)
                    .gatewayId(accessType == AccessType.GATEWAY_SUB_DEVICE ? gatewayId : null)
                    .build();
            index.put(key, route);
            log.info("加载设备路由，productKey={}，deviceCode={}，accessType={}，gatewayId={}",
                    productKey, deviceCode, accessType, gatewayId);
        }

        routeIndex = index;
        log.info("设备路由加载完成，共 {} 台设备", routeIndex.size());
    }

    @Override
    public DeviceRoute resolve(String productKey, String deviceCode) {
        String key = buildKey(productKey, deviceCode);
        DeviceRoute route = routeIndex.get(key);
        if (route == null) {
            log.warn("目标设备不存在，productKey={}，deviceCode={}", productKey, deviceCode);
            throw new HttpStatus4xxException(40400, "目标设备不存在");
        }
        if (route.getAccessType() == AccessType.GATEWAY_SUB_DEVICE && !StringUtils.hasText(route.getGatewayId())) {
            log.warn("设备网关路由无效，productKey={}，deviceCode={}", productKey, deviceCode);
            throw new HttpStatus4xxException(40900, "设备网关路由无效");
        }
        return route;
    }

    private static String buildKey(String productKey, String deviceCode) {
        return productKey + ":" + deviceCode;
    }
}
