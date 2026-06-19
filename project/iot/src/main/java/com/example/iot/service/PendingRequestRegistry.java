package com.example.iot.service;

import com.example.iot.config.InvokeProperties;
import com.example.iot.exception.DuplicatePendingRequestException;
import com.example.iot.exception.InvokeTimeoutException;
import com.example.iot.exception.PendingRequestLimitExceededException;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 内存注册表，用于关联消息 ID 和待处理 HTTP 请求。
 */
@Slf4j
@Service
public class PendingRequestRegistry {

    private final ConcurrentMap<Long, PendingRequest> pendingRequests = new ConcurrentHashMap<>();

    private final ScheduledExecutorService timeoutExecutor;

    private final Semaphore pendingPermits;

    private final int maxPending;

    /**
     * 使用应用配置创建注册表。
     *
     * @param properties 调用配置
     */
    @Autowired
    public PendingRequestRegistry(InvokeProperties properties) {
        this(properties.getMaxPending());
    }

    /**
     * 使用固定待处理请求上限创建注册表。
     *
     * @param maxPending 最大待处理请求数
     */
    public PendingRequestRegistry(int maxPending) {
        this.maxPending = maxPending;
        this.pendingPermits = new Semaphore(maxPending);
        this.timeoutExecutor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable, "invoke-timeout-scanner");
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * 注册一个待处理请求。
     *
     * @param msgId 消息 ID
     * @param timeout 请求超时时间
     * @return 待处理请求
     */
    public PendingRequest register(long msgId, Duration timeout) {
        if (!pendingPermits.tryAcquire()) {
            log.warn("待处理请求数量达到上限，msgId={}，maxPending={}", msgId, maxPending);
            throw new PendingRequestLimitExceededException(maxPending);
        }

        PendingRequest request = new PendingRequest(msgId, new CompletableFuture<>());
        PendingRequest previous = pendingRequests.putIfAbsent(msgId, request);
        if (previous != null) {
            pendingPermits.release();
            log.warn("待处理请求重复，msgId={}", msgId);
            throw new DuplicatePendingRequestException(msgId);
        }

        request.setTimeoutFuture(timeoutExecutor.schedule(() -> timeout(msgId, request),
                timeout.toMillis(), TimeUnit.MILLISECONDS));
        return request;
    }

    /**
     * 使用回复数据完成待处理请求。
     *
     * @param msgId 消息 ID
     * @param data 回复数据
     * @return 是否匹配到待处理请求
     */
    public boolean complete(long msgId, String data) {
        PendingRequest request = pendingRequests.remove(msgId);
        if (request == null) {
            log.warn("收到未知 msgId 的 MQTT 回复，msgId={}", msgId);
            return false;
        }

        cleanupAfterRemove(request);
        String replyData = StringUtils.hasLength(data) ? data : "";
        boolean completed = request.getFuture().complete(replyData);
        log.info("待处理请求已完成，msgId={}，completed={}", msgId, completed);
        return completed;
    }

    /**
     * 使用异常结束待处理请求。
     *
     * @param msgId 消息 ID
     * @param throwable 失败原因
     * @return 是否匹配到待处理请求
     */
    public boolean fail(long msgId, Throwable throwable) {
        PendingRequest request = pendingRequests.remove(msgId);
        if (request == null) {
            return false;
        }

        cleanupAfterRemove(request);
        return request.getFuture().completeExceptionally(throwable);
    }

    /**
     * 取消并移除一个待处理请求。
     *
     * @param msgId 消息 ID
     * @return 是否移除了待处理请求
     */
    public boolean cancel(long msgId) {
        PendingRequest request = pendingRequests.remove(msgId);
        if (request == null) {
            return false;
        }

        cleanupAfterRemove(request);
        return request.getFuture().cancel(false);
    }

    /**
     * 返回当前待处理请求数量。
     *
     * @return 待处理请求数量
     */
    public int size() {
        return pendingRequests.size();
    }

    /**
     * 关闭超时处理资源。
     */
    @PreDestroy
    public void shutdown() {
        timeoutExecutor.shutdownNow();
    }

    private void timeout(long msgId, PendingRequest request) {
        boolean removed = pendingRequests.remove(msgId, request);
        if (!removed) {
            return;
        }

        pendingPermits.release();
        log.warn("调用请求等待超时，msgId={}", msgId);
        request.getFuture().completeExceptionally(new InvokeTimeoutException(msgId));
    }

    private void cleanupAfterRemove(PendingRequest request) {
        ScheduledFuture<?> timeoutFuture = request.getTimeoutFuture();
        if (timeoutFuture != null) {
            timeoutFuture.cancel(false);
        }
        pendingPermits.release();
    }
}
