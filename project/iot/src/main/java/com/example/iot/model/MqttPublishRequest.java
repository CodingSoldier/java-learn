package com.example.iot.model;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MQTT 发布请求，封装发布所需的全部信息。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MqttPublishRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 发布目标 Topic。
     */
    private String topic;

    /**
     * MQTT payload 字节。
     */
    private byte[] payload;

    /**
     * MQTT 5 响应 Topic，可选。
     */
    private String responseTopic;

    /**
     * MQTT 5 关联数据，使用 msgId 的 UTF-8 字节，可选。
     */
    private byte[] correlationData;
}
