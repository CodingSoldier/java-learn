package com.example.iot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.github.codingsoldier.common.exception.HttpStatus4xxException;
import com.github.codingsoldier.common.exception.HttpStatus5xxException;
import java.time.Duration;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * {@link PendingRequestRegistry} 的测试。
 */
class PendingRequestRegistryTest {

    private PendingRequestRegistry registry;

    /**
     * 每个测试结束后关闭注册表资源。
     */
    @AfterEach
    void tearDown() {
        if (registry != null) {
            registry.shutdown();
        }
    }

    /**
     * 注册、完成并清理一个待处理请求。
     *
     * @throws Exception 读取 future 失败时抛出
     */
    @Test
    void shouldRegisterCompleteAndCleanup() throws Exception {
        registry = new PendingRequestRegistry(10);
        PendingRequest request = registry.register(1L, Duration.ofSeconds(1));

        boolean matched = registry.complete(1L, "返回的数据");

        assertThat(matched).isTrue();
        assertThat(request.getFuture().get(1, TimeUnit.SECONDS)).isEqualTo("返回的数据");
        assertThat(registry.size()).isZero();
    }

    /**
     * 让待处理请求超时并完成清理。
     */
    @Test
    void shouldTimeoutAndCleanup() {
        registry = new PendingRequestRegistry(10);
        PendingRequest request = registry.register(1L, Duration.ofMillis(30));

        assertThatThrownBy(() -> request.getFuture().get(1, TimeUnit.SECONDS))
                .isInstanceOf(ExecutionException.class)
                .hasCauseInstanceOf(HttpStatus5xxException.class);
        assertThat(registry.size()).isZero();
    }

    /**
     * 对未知消息 ID 返回 false。
     */
    @Test
    void shouldReturnFalseForUnknownMsgId() {
        registry = new PendingRequestRegistry(10);

        boolean matched = registry.complete(999L, "返回的数据");

        assertThat(matched).isFalse();
    }

    /**
     * 首次完成后忽略重复回复。
     *
     * @throws Exception 读取 future 失败时抛出
     */
    @Test
    void shouldIgnoreDuplicatedReply() throws Exception {
        registry = new PendingRequestRegistry(10);
        PendingRequest request = registry.register(1L, Duration.ofSeconds(1));

        boolean firstMatched = registry.complete(1L, "第一次返回");
        boolean secondMatched = registry.complete(1L, "第二次返回");

        assertThat(firstMatched).isTrue();
        assertThat(secondMatched).isFalse();
        assertThat(request.getFuture().get(1, TimeUnit.SECONDS)).isEqualTo("第一次返回");
        assertThat(registry.size()).isZero();
    }

    /**
     * 待处理请求达到上限时拒绝新请求。
     */
    @Test
    void shouldRejectWhenMaxPendingExceeded() {
        registry = new PendingRequestRegistry(1);
        registry.register(1L, Duration.ofSeconds(1));

        assertThatThrownBy(() -> registry.register(2L, Duration.ofSeconds(1)))
                .isInstanceOf(HttpStatus4xxException.class);
        assertThat(registry.size()).isOne();
    }
}
