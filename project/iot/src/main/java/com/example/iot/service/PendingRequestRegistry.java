package com.example.iot.service;

import com.example.iot.config.InvokeProperties;
import com.example.iot.model.ServiceResponseMessage;
import com.github.codingsoldier.common.enums.ResultCodeEnum;
import com.github.codingsoldier.common.exception.HttpStatus4xxException;
import com.github.codingsoldier.common.exception.HttpStatus5xxException;
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

/**
 * 内存注册表，用于关联消息 ID 和待处理 HTTP 请求。
 */
@Slf4j
@Service
public class PendingRequestRegistry {

    private final ConcurrentMap<String, PendingRequest> pendingRequests = new ConcurrentHashMap<>();

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
     * @param msgId            消息 ID
     * @param timeout          请求超时时间
     * @param expectedProductKey 预期的产品标识
     * @param expectedDeviceCode 预期的设备编码
     * @param expectedGatewayId  预期的网关标识，直连设备传 null
     * @param expectedServiceCode 预期的服务编码
     * @return 待处理请求
     */
    public PendingRequest register(String msgId, Duration timeout,
                                   String expectedProductKey, String expectedDeviceCode,
                                   String expectedGatewayId, String expectedServiceCode) {
        if (!pendingPermits.tryAcquire()) {
            log.warn("待处理请求数量达到上限，msgId={}，maxPending={}", msgId, maxPending);
            throw new HttpStatus4xxException(ResultCodeEnum.TOO_MANY_REQUESTS, "待处理请求数量达到上限");
        }

        PendingRequest request = new PendingRequest(msgId, expectedProductKey, expectedDeviceCode,
                expectedGatewayId, expectedServiceCode, new CompletableFuture<>());
        PendingRequest previous = pendingRequests.putIfAbsent(msgId, request);
        if (previous != null) {
            pendingPermits.release();
            log.warn("待处理请求重复，msgId={}", msgId);
            throw new HttpStatus4xxException(40900, "msgId 重复");
        }

        request.setTimeoutFuture(timeoutExecutor.schedule(() -> timeout(msgId, request),
                timeout.toMillis(), TimeUnit.MILLISECONDS));
        return request;
    }

    /**
     * 使用回复数据完成待处理请求。
     *
     * @param msgId   消息 ID
     * @param message 服务响应消息
     * @return 是否匹配到待处理请求
     */
    public boolean complete(String msgId, ServiceResponseMessage message) {
        PendingRequest request = pendingRequests.remove(msgId);
        if (request == null) {
            log.warn("收到未知 msgId 的 MQTT 回复，msgId={}", msgId);
            return false;
        }

        cleanupAfterRemove(request);
        boolean completed = request.getFuture().complete(message);
        log.info("待处理请求已完成，msgId={}，completed={}", msgId, completed);
        return completed;
    }

    /**
     * 使用异常结束待处理请求。
     *
     * @param msgId     消息 ID
     * @param throwable 失败原因
     * @return 是否匹配到待处理请求
     */
    public boolean fail(String msgId, Throwable throwable) {
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
    public boolean cancel(String msgId) {
        PendingRequest request = pendingRequests.remove(msgId);
        if (request == null) {
            return false;
        }

        cleanupAfterRemove(request);
        return request.getFuture().cancel(false);
    }

    /**
     * 根据 msgId 获取待处理请求。
     *
     * @param msgId 消息 ID
     * @return 待处理请求，不存在时返回 null
     */
    public PendingRequest get(String msgId) {
        return pendingRequests.get(msgId);
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

    private void timeout(String msgId, PendingRequest request) {
        boolean removed = pendingRequests.remove(msgId, request);
        if (!removed) {
            return;
        }

        pendingPermits.release();
        log.warn("调用请求等待超时，msgId={}", msgId);
        request.getFuture().completeExceptionally(new HttpStatus5xxException(50400, "等待 MQTT 回复超时"));
    }

    private void cleanupAfterRemove(PendingRequest request) {
        ScheduledFuture<?> timeoutFuture = request.getTimeoutFuture();
        if (timeoutFuture != null) {
            timeoutFuture.cancel(false);
        }
        pendingPermits.release();
    }
}
