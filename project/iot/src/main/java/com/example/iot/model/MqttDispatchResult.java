package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT 上游消息分发结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqttDispatchResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 从 payload 中解析的消息 ID；解析失败时为空字符串。
     */
    @Builder.Default
    private String msgId = "";

    /**
     * 消息是否成功匹配并分发到处理器。
     */
    private boolean matched;
}
