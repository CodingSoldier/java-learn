package com.example.iot.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.iot.model.ServiceResponseMessage;
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
        PendingRequest request = registry.register("123", Duration.ofSeconds(1),
                "light", "light001", null, "switch");

        ServiceResponseMessage response = ServiceResponseMessage.builder()
                .msgId("123")
                .code(20000)
                .message("成功")
                .build();
        boolean matched = registry.complete("123", response);

        assertThat(matched).isTrue();
        assertThat(request.getFuture().get(1, TimeUnit.SECONDS)).isEqualTo(response);
        assertThat(registry.size()).isZero();
    }

    /**
     * 让待处理请求超时并完成清理。
     */
    @Test
    void shouldTimeoutAndCleanup() {
        registry = new PendingRequestRegistry(10);
        PendingRequest request = registry.register("123", Duration.ofMillis(30),
                "light", "light001", null, "switch");

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

        ServiceResponseMessage response = ServiceResponseMessage.builder()
                .msgId("999")
                .code(20000)
                .build();
        boolean matched = registry.complete("999", response);

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
        PendingRequest request = registry.register("123", Duration.ofSeconds(1),
                "light", "light001", null, "switch");

        ServiceResponseMessage firstResponse = ServiceResponseMessage.builder()
                .msgId("123").code(20000).message("第一次返回").build();
        ServiceResponseMessage secondResponse = ServiceResponseMessage.builder()
                .msgId("123").code(20000).message("第二次返回").build();

        boolean firstMatched = registry.complete("123", firstResponse);
        boolean secondMatched = registry.complete("123", secondResponse);

        assertThat(firstMatched).isTrue();
        assertThat(secondMatched).isFalse();
        assertThat(request.getFuture().get(1, TimeUnit.SECONDS)).isEqualTo(firstResponse);
        assertThat(registry.size()).isZero();
    }

    /**
     * 待处理请求达到上限时拒绝新请求。
     */
    @Test
    void shouldRejectWhenMaxPendingExceeded() {
        registry = new PendingRequestRegistry(1);
        registry.register("123", Duration.ofSeconds(1),
                "light", "light001", null, "switch");

        assertThatThrownBy(() -> registry.register("456", Duration.ofSeconds(1),
                "light", "light001", null, "switch"))
                .isInstanceOf(HttpStatus4xxException.class);
        assertThat(registry.size()).isOne();
    }

    /**
     * 注册请求应保存预期的设备身份信息。
     */
    @Test
    void shouldStoreExpectedDeviceIdentity() {
        registry = new PendingRequestRegistry(10);
        PendingRequest request = registry.register("123", Duration.ofSeconds(1),
                "sensor", "sensor001", "gw001", "switch");

        assertThat(request.getExpectedProductKey()).isEqualTo("sensor");
        assertThat(request.getExpectedDeviceCode()).isEqualTo("sensor001");
        assertThat(request.getExpectedGatewayId()).isEqualTo("gw001");
        assertThat(request.getExpectedServiceCode()).isEqualTo("switch");
    }
}
