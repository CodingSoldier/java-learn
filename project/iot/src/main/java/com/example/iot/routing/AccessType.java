package com.example.iot.routing;

/**
 * 设备接入类型枚举。
 */
public enum AccessType {

    /**
     * 直连设备，设备直接连接 EMQX。
     */
    DIRECT_DEVICE,

    /**
     * 网关子设备，通过网关代理接入。
     */
    GATEWAY_SUB_DEVICE
}
