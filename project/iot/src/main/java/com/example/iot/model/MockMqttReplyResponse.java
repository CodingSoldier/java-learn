package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 模拟 MQTT 回复接口返回的响应。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MockMqttReplyResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 回复携带的消息 ID。
     */
    private Long msgId;

    /**
     * 是否匹配到了待处理的 HTTP 请求。
     */
    private boolean matched;
}
