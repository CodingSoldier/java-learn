package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 服务调用成功时的 HTTP 响应。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceInvokeResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 模拟 MQTT 回复返回的数据。
     */
    private String data;
}
