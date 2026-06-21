package com.example.iot.service;

import com.example.iot.common.MsgIdGenerator;
import com.example.iot.config.InvokeProperties;
import com.example.iot.model.DirectServiceRequestMessage;
import com.example.iot.model.GatewayServiceRequestMessage;
import com.example.iot.model.MqttPublishRequest;
import com.example.iot.model.ServiceInvokeRequest;
import com.example.iot.model.ServiceInvokeResponse;
import com.example.iot.model.ServiceResponseMessage;
import com.example.iot.mqtt.HiveMqttGateway;
import com.example.iot.mqtt.MqttTopicResolver;
import com.example.iot.routing.AccessType;
import com.example.iot.routing.DeviceRoute;
import com.example.iot.routing.DeviceRouteResolver;
import com.github.codingsoldier.common.enums.ResultCodeEnum;
import com.github.codingsoldier.common.exception.HttpStatus5xxException;
import com.github.codingsoldier.common.exception.MicroServiceException;
import com.github.codingsoldier.common.resp.Result;
import com.github.codingsoldier.common.util.CommonUtil;
import com.github.codingsoldier.common.util.objectmapper.ObjectMapperUtil;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
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

    private static final int CODE_SUCCESS = 20000;

    private final MsgIdGenerator msgIdGenerator;

    private final InvokeProperties invokeProperties;

    private final PendingRequestRegistry pendingRequestRegistry;

    private final HiveMqttGateway mqttGateway;

    private final DeviceRouteResolver deviceRouteResolver;

    /**
     * 调用 IoT 服务，并异步等待匹配的 MQTT 回复。
     *
     * @param request 调用请求
     * @return 延迟 HTTP 响应
     */
    public DeferredResult<ResponseEntity<?>> invoke(ServiceInvokeRequest request) {
        // 解析设备路由
        DeviceRoute route = deviceRouteResolver.resolve(request.getProductKey(), request.getDeviceCode());
        String msgId = msgIdGenerator.nextId();
        long timestamp = Instant.now().toEpochMilli();

        // 构造发布请求
        MqttPublishRequest publishRequest = buildPublishRequest(route, request, msgId, timestamp);
        log.info("MQTT 服务调用，msgId={}，productKey={}，deviceCode={}，serviceCode={}，accessType={}",
                msgId, request.getProductKey(), request.getDeviceCode(), request.getServiceCode(), route.getAccessType());

        // 注册待处理请求
        PendingRequest pendingRequest = pendingRequestRegistry.register(msgId, invokeProperties.getTimeout(),
                request.getProductKey(), request.getDeviceCode(),
                route.getAccessType() == AccessType.GATEWAY_SUB_DEVICE ? route.getGatewayId() : null,
                request.getServiceCode());

        DeferredResult<ResponseEntity<?>> result = new DeferredResult<>(deferredTimeoutMillis(invokeProperties.getTimeout()));
        pendingRequest.getFuture().whenComplete((responseMsg, throwable) -> completeDeferredResult(result, msgId, responseMsg, throwable));
        result.onCompletion(() -> pendingRequestRegistry.cancel(msgId));
        result.onTimeout(() -> {
            log.warn("Servlet 异步请求先于注册表完成前超时，msgId={}", msgId);
            pendingRequestRegistry.fail(msgId, new HttpStatus5xxException(CODE_MQTT_REPLY_TIMEOUT, "等待 MQTT 回复超时"));
        });

        try {
            mqttGateway.publish(publishRequest).whenComplete((ignored, throwable) -> {
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

    private MqttPublishRequest buildPublishRequest(DeviceRoute route, ServiceInvokeRequest request,
                                                    String msgId, long timestamp) {
        if (route.getAccessType() == AccessType.DIRECT_DEVICE) {
            return buildDirectPublishRequest(route, request, msgId, timestamp);
        } else {
            return buildGatewayPublishRequest(route, request, msgId, timestamp);
        }
    }

    private MqttPublishRequest buildDirectPublishRequest(DeviceRoute route, ServiceInvokeRequest request,
                                                          String msgId, long timestamp) {
        String topic = MqttTopicResolver.directServiceRequest(
                route.getProductKey(), route.getDeviceCode(), request.getServiceCode());

        DirectServiceRequestMessage message = DirectServiceRequestMessage.builder()
                .msgId(msgId)
                .timestamp(timestamp)
                .data(request.getData())
                .build();

        byte[] payload = ObjectMapperUtil.writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
        byte[] correlationData = msgId.getBytes(StandardCharsets.UTF_8);

        return MqttPublishRequest.builder()
                .topic(topic)
                .payload(payload)
                .correlationData(correlationData)
                .build();
    }

    private MqttPublishRequest buildGatewayPublishRequest(DeviceRoute route, ServiceInvokeRequest request,
                                                           String msgId, long timestamp) {
        String topic = MqttTopicResolver.gatewayServiceRequest(route.getGatewayId(), request.getServiceCode());

        GatewayServiceRequestMessage message = GatewayServiceRequestMessage.builder()
                .msgId(msgId)
                .timestamp(timestamp)
                .target(GatewayServiceRequestMessage.Target.builder()
                        .productKey(route.getProductKey())
                        .deviceCode(route.getDeviceCode())
                        .build())
                .data(request.getData())
                .build();

        byte[] payload = ObjectMapperUtil.writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
        byte[] correlationData = msgId.getBytes(StandardCharsets.UTF_8);

        return MqttPublishRequest.builder()
                .topic(topic)
                .payload(payload)
                .correlationData(correlationData)
                .build();
    }

    private void completeDeferredResult(DeferredResult<ResponseEntity<?>> result, String msgId,
                                        ServiceResponseMessage responseMsg, Throwable throwable) {
        if (throwable == null) {
            // 成功：返回 code=20000 时 HTTP 200 + Result.success
            if (responseMsg.getCode() != null && responseMsg.getCode() == CODE_SUCCESS) {
                result.setResult(ResponseEntity.ok(Result.success(ServiceInvokeResponse.builder()
                        .data(responseMsg.getData() != null ? responseMsg.getData() : new java.util.HashMap<>())
                        .build())));
            } else {
                // 非 20000：使用 code 计算 HTTP Status
                int code = responseMsg.getCode() != null ? responseMsg.getCode() : 50000;
                String message = responseMsg.getMessage() != null ? responseMsg.getMessage() : "设备处理失败";
                result.setResult(ResponseEntity.status(CommonUtil.getResponseStatus(code))
                        .body(Result.fail(code, message)));
            }
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
