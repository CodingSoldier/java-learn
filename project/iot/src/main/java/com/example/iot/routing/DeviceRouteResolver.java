package com.example.iot.routing;

/**
 * 设备路由解析器，根据产品标识和设备编码解析设备的接入路由。
 */
public interface DeviceRouteResolver {

    /**
     * 解析设备路由。
     *
     * @param productKey 产品标识
     * @param deviceCode 设备编码
     * @return 设备路由
     * @throws com.github.codingsoldier.common.exception.HttpStatus4xxException 设备不存在或路由无效时
     */
    DeviceRoute resolve(String productKey, String deviceCode);
}
