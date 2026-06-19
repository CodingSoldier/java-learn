package com.example.iot.service;

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

    private final long msgId;

    private final CompletableFuture<String> future;

    @Setter
    private ScheduledFuture<?> timeoutFuture;
}
