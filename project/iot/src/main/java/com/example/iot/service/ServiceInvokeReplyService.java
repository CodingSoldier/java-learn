package com.example.iot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 服务调用回复处理服务。
 */
@Service
@RequiredArgsConstructor
public class ServiceInvokeReplyService {

    private final PendingRequestRegistry pendingRequestRegistry;

    /**
     * 使用回复数据完成待处理调用请求。
     *
     * @param msgId 消息 ID
     * @param data 回复数据
     * @return 是否匹配到待处理请求
     */
    public boolean completeReply(long msgId, String data) {
        return pendingRequestRegistry.complete(msgId, data);
    }
}
