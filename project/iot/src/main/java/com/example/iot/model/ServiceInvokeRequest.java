package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IoT 服务调用的 HTTP 请求。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInvokeRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 设备产品标识。
     */
    @NotBlank(message = "产品标识不能为空")
    private String productKey;

    /**
     * 设备编码。
     */
    @NotBlank(message = "设备编码不能为空")
    private String deviceCode;

    /**
     * 服务编码。
     */
    @NotBlank(message = "服务编码不能为空")
    private String serviceCode;

    /**
     * 服务调用参数，JSON 对象类型；缺省时归一化为空 Map。
     */
    @NotNull(message = "服务参数不能为空")
    @Builder.Default
    private Map<String, Object> data = new HashMap<>();
}
