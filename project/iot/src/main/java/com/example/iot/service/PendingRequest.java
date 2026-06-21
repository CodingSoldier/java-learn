package com.example.iot.service;

import com.example.iot.model.ServiceResponseMessage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * 通过消息 ID 跟踪的待处理调用请求。
 */
@Getter
@RequiredArgsConstructor
public class PendingRequest {

    /**
     * 消息 ID，Snowflake 十进制字符串。
     */
    private final String msgId;

    /**
     * 预期的产品标识。
     */
    private final String expectedProductKey;

    /**
     * 预期的设备编码。
     */
    private final String expectedDeviceCode;

    /**
     * 预期的网关标识，直连设备为 null。
     */
    private final String expectedGatewayId;

    /**
     * 预期的服务编码。
     */
    private final String expectedServiceCode;

    /**
     * 异步完成的 Future，保存完整设备响应。
     */
    private final CompletableFuture<ServiceResponseMessage> future;

    /**
     * 超时调度任务。
     */
    @Setter
    private ScheduledFuture<?> timeoutFuture;
}
