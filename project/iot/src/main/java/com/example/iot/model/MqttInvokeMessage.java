package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 发送到模拟 MQTT 调用主题的消息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqttInvokeMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用于关联后续回复的消息 ID。
     */
    private Long msgId;

    /**
     * 载荷数据。
     */
    private String data;
}
