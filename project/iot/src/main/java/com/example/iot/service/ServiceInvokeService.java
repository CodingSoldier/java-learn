package com.example.iot.service;

import com.example.iot.common.MsgIdGenerator;
import com.example.iot.config.InvokeProperties;
import com.example.iot.model.MqttInvokeMessage;
import com.example.iot.model.ServiceInvokeRequest;
import com.example.iot.model.ServiceInvokeResponse;
import com.example.iot.mqtt.MqttGateway;
import com.github.codingsoldier.common.enums.ResultCodeEnum;
import com.github.codingsoldier.common.exception.HttpStatus5xxException;
import com.github.codingsoldier.common.exception.MicroServiceException;
import com.github.codingsoldier.common.resp.Result;
import com.github.codingsoldier.common.util.CommonUtil;
import java.time.Duration;
import java.util.concurrent.CompletionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 编排 HTTP 服务调用请求和 MQTT 回复。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServiceInvokeService {

    private static final int CODE_MQTT_REPLY_TIMEOUT = 50400;

    private final MsgIdGenerator msgIdGenerator;

    private final InvokeProperties invokeProperties;

    private final PendingRequestRegistry pendingRequestRegistry;

    private final MqttReplyHandler mqttReplyHandler;

    private final MqttGateway mqttGateway;

    /**
     * 调用 IoT 服务，并异步等待匹配的 MQTT 回复。
     *
     * @param request 调用请求
     * @return 延迟 HTTP 响应
     */
    public DeferredResult<ResponseEntity<?>> invoke(ServiceInvokeRequest request) {
        long msgId = msgIdGenerator.nextId();
        PendingRequest pendingRequest = pendingRequestRegistry.register(msgId, invokeProperties.getTimeout());

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(deferredTimeoutMillis(invokeProperties.getTimeout()));
        pendingRequest.getFuture().whenComplete((data, throwable) -> completeDeferredResult(result, msgId, data, throwable));
        result.onCompletion(() -> pendingRequestRegistry.cancel(msgId));
        result.onTimeout(() -> {
            log.warn("Servlet 异步请求先于注册表完成前超时，msgId={}", msgId);
            pendingRequestRegistry.fail(msgId, new HttpStatus5xxException(CODE_MQTT_REPLY_TIMEOUT, "等待 MQTT 回复超时"));
        });

        try {
            mqttGateway.sendInvoke(MqttInvokeMessage.builder()
                    .msgId(msgId)
                    .data(request.getData())
                    .build()).whenComplete((ignored, throwable) -> {
                        if (throwable == null) {
                            return;
                        }
                        log.error("发送 MQTT 调用消息失败，msgId={}", msgId, throwable);
                        pendingRequestRegistry.fail(msgId,
                                new HttpStatus5xxException(ResultCodeEnum.BACKEND_SERVER_ERROR, "发送 MQTT 调用消息失败"));
                    });
        } catch (RuntimeException ex) {
            log.error("发送 MQTT 调用消息失败，msgId={}", msgId, ex);
            pendingRequestRegistry.fail(msgId,
                    new HttpStatus5xxException(ResultCodeEnum.BACKEND_SERVER_ERROR, "发送 MQTT 调用消息失败"));
        }

        return result;
    }

    /**
     * 使用 MQTT 回复数据完成待处理调用请求。
     *
     * @param msgId 消息 ID
     * @param data 回复数据
     * @return 是否匹配到待处理请求
     */
    public boolean completeReply(long msgId, String data) {
        return mqttReplyHandler.completeReply(msgId, data);
    }

    private void completeDeferredResult(DeferredResult<ResponseEntity<?>> result, long msgId,
                                        String data, Throwable throwable) {
        if (throwable == null) {
            result.setResult(ResponseEntity.ok(Result.success(ServiceInvokeResponse.builder()
                    .data(data == null ? "" : data)
                    .build())));
            return;
        }

        Throwable cause = unwrap(throwable);
        if (cause instanceof MicroServiceException microServiceException) {
            result.setResult(ResponseEntity.status(CommonUtil.getResponseStatus(microServiceException.getCode()))
                    .body(Result.fail(microServiceException.getCode(), microServiceException.getMessage())));
            return;
        }

        log.error("调用请求失败，msgId={}", msgId, cause);
        result.setResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Result.fail(ResultCodeEnum.SERVER_ERROR.getCode(), "调用请求失败")));
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
