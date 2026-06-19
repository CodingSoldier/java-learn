package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 从模拟 MQTT 回复主题收到的消息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqttReplyMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用于关联原始 HTTP 请求的消息 ID。
     */
    @NotNull(message = "msgId 不能为空")
    private Long msgId;

    /**
     * 回复载荷数据。
     */
    private String data;
}
