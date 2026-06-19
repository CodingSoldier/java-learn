package com.example.iot.service;

import com.example.iot.common.ApiErrorResponse;
import com.example.iot.common.MsgIdGenerator;
import com.example.iot.config.InvokeProperties;
import com.example.iot.exception.DuplicatePendingRequestException;
import com.example.iot.exception.InvokeTimeoutException;
import com.example.iot.exception.PendingRequestLimitExceededException;
import com.example.iot.model.MqttInvokeMessage;
import com.example.iot.model.ServiceInvokeRequest;
import com.example.iot.model.ServiceInvokeResponse;
import com.example.iot.mqtt.MqttGateway;
import java.time.Duration;
import java.util.concurrent.CompletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 编排 HTTP 服务调用请求和模拟 MQTT 回复。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceInvokeService {

    private static final String CODE_TIMEOUT = "TIMEOUT";

    private static final String CODE_TOO_MANY_PENDING = "TOO_MANY_PENDING";

    private static final String CODE_DUPLICATE_MSG_ID = "DUPLICATE_MSG_ID";

    private static final String CODE_INTERNAL_ERROR = "INTERNAL_ERROR";

    private final MsgIdGenerator msgIdGenerator;

    private final InvokeProperties invokeProperties;

    private final PendingRequestRegistry pendingRequestRegistry;

    private final MqttGateway mqttGateway;

    /**
     * 调用模拟 IoT 服务，并异步等待匹配的回复。
     *
     * @param request 调用请求
     * @return 延迟 HTTP 响应
     */
    public DeferredResult<ResponseEntity<?>> invoke(ServiceInvokeRequest request) {
        long msgId = msgIdGenerator.nextId();
        PendingRequest pendingRequest;
        try {
            pendingRequest = pendingRequestRegistry.register(msgId, invokeProperties.getTimeout());
        } catch (PendingRequestLimitExceededException ex) {
            log.warn("拒绝调用请求，原因：待处理请求数量达到上限，msgId={}", msgId, ex);
            return completed(HttpStatus.TOO_MANY_REQUESTS, CODE_TOO_MANY_PENDING, "待处理请求数量达到上限");
        } catch (DuplicatePendingRequestException ex) {
            log.warn("拒绝调用请求，原因：msgId 重复，msgId={}", msgId, ex);
            return completed(HttpStatus.CONFLICT, CODE_DUPLICATE_MSG_ID, "msgId 重复");
        }

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(deferredTimeoutMillis(invokeProperties.getTimeout()));
        pendingRequest.getFuture().whenComplete((data, throwable) -> completeDeferredResult(result, msgId, data, throwable));
        result.onCompletion(() -> pendingRequestRegistry.cancel(msgId));
        result.onTimeout(() -> {
            log.warn("Servlet 异步请求先于注册表完成前超时，msgId={}", msgId);
            pendingRequestRegistry.fail(msgId, new InvokeTimeoutException(msgId));
        });

        try {
            mqttGateway.sendInvoke(MqttInvokeMessage.builder()
                    .msgId(msgId)
                    .data(request.getData())
                    .build());
        } catch (RuntimeException ex) {
            log.error("发送模拟 MQTT 调用消息失败，msgId={}", msgId, ex);
            pendingRequestRegistry.fail(msgId, ex);
        }

        return result;
    }

    /**
     * 使用模拟 MQTT 回复数据完成待处理调用请求。
     *
     * @param msgId 消息 ID
     * @param data 回复数据
     * @return 是否匹配到待处理请求
     */
    public boolean completeReply(long msgId, String data) {
        return pendingRequestRegistry.complete(msgId, data);
    }

    private void completeDeferredResult(DeferredResult<ResponseEntity<?>> result, long msgId,
                                        String data, Throwable throwable) {
        if (throwable == null) {
            result.setResult(ResponseEntity.ok(ServiceInvokeResponse.builder()
                    .data(data == null ? "" : data)
                    .build()));
            return;
        }

        Throwable cause = unwrap(throwable);
        if (cause instanceof InvokeTimeoutException) {
            result.setResult(ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT)
                    .body(error(CODE_TIMEOUT, "等待 MQTT 回复超时")));
            return;
        }

        log.error("调用请求失败，msgId={}", msgId, cause);
        result.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error(CODE_INTERNAL_ERROR, "调用请求失败")));
    }

    private DeferredResult<ResponseEntity<?>> completed(HttpStatus status, String code, String message) {
        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>();
        result.setResult(ResponseEntity.status(status).body(error(code, message)));
        return result;
    }

    private ApiErrorResponse error(String code, String message) {
        return ApiErrorResponse.builder()
                .code(code)
                .message(message)
                .build();
    }

    private long deferredTimeoutMillis(Duration timeout) {
        return timeout.plusSeconds(1).toMillis();
    }

    private Throwable unwrap(Throwable throwable) {
        if (throwable instanceof CompletionException && throwable.getCause() != null) {
            return throwable.getCause();
        }
        return throwable;
    }
}
