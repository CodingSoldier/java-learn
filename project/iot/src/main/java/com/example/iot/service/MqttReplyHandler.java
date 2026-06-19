package com.example.iot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * MQTT 回复消息处理器。
 */
@Service
@RequiredArgsConstructor
public class MqttReplyHandler {

    private final PendingRequestRegistry pendingRequestRegistry;

    /**
     * 使用 MQTT 回复数据完成待处理调用请求。
     *
     * @param msgId 消息 ID
     * @param data 回复数据
     * @return 是否匹配到待处理请求
     */
    public boolean completeReply(long msgId, String data) {
        return pendingRequestRegistry.complete(msgId, data);
    }
}
