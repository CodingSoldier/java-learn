package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 直连设备服务调用请求消息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectServiceRequestMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息 ID，Snowflake 十进制字符串。
     */
    private String msgId;

    /**
     * 消息发送时间，UTC Unix 毫秒时间戳。
     */
    private Long timestamp;

    /**
     * 服务调用参数，JSON 对象类型；缺省时归一化为空 Map。
     */
    @Builder.Default
    private Map<String, Object> data = new HashMap<>();
}
