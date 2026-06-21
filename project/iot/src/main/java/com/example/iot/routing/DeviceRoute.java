package com.example.iot.routing;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 设备路由信息，描述设备的接入方式和路由参数。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRoute implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 接入类型。
     */
    private AccessType accessType;

    /**
     * 产品标识。
     */
    private String productKey;

    /**
     * 设备编码。
     */
    private String deviceCode;

    /**
     * 网关标识，仅网关子设备有效。
     */
    private String gatewayId;
}
