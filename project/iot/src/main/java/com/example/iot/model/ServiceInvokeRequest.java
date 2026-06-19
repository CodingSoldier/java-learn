package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotBlank;
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
     * 需要发送到下游 MQTT 主题的数据。
     */
    @NotBlank(message = "data must not be blank")
    private String data;
}
