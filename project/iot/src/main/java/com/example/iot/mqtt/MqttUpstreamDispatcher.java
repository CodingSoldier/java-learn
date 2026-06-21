package com.example.iot.mqtt;

import com.example.iot.model.MqttDispatchResult;
import com.example.iot.model.MqttTopicMetadata;
import com.example.iot.model.ServiceResponseMessage;
import com.github.codingsoldier.common.util.objectmapper.ObjectMapperUtil;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * MQTT 上游消息分发器。
 * <p>
 * 根据实际 Topic 匹配消息类型，不包含业务处理。
 * 网关消息必须校验 Topic 中的 gatewayId 与 payload 中的子设备身份。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MqttUpstreamDispatcher {

    private static final long MAX_CLOCK_SKEW_MILLIS = Duration.ofMinutes(5).toMillis();

    private final ServiceResponseHandler serviceResponseHandler;

    /**
     * 分发 MQTT 上游消息。
     *
     * @param topic       完整 Topic
     * @param payloadBytes MQTT payload 字节
     * @return 分发结果
     */
    public MqttDispatchResult dispatch(String topic, byte[] payloadBytes) {
        MqttTopicMetadata metadata = MqttTopicResolver.parse(topic);
        if (!metadata.isValid()) {
            log.warn("无法解析 Topic，topic={}", topic);
            return MqttDispatchResult.builder().msgId("").matched(false).build();
        }

        String messageType = metadata.getMessageType();
        if (messageType == null || messageType.isEmpty()) {
            log.warn("无法识别消息类型，topic={}", topic);
            return MqttDispatchResult.builder().msgId("").matched(false).build();
        }

        try {
            if ("services-response".equals(messageType)) {
                return dispatchServiceResponse(metadata, payloadBytes, topic);
            }
            log.warn("暂不支持的消息类型，messageType={}，topic={}", messageType, topic);
            return MqttDispatchResult.builder().msgId("").matched(false).build();
        } catch (Exception ex) {
            log.error("消息分发异常，topic={}", topic, ex);
            return MqttDispatchResult.builder().msgId("").matched(false).build();
        }
    }

    @SuppressWarnings("unchecked")
    private MqttDispatchResult dispatchServiceResponse(MqttTopicMetadata metadata, byte[] payloadBytes, String topic) {
        // 反序列化 payload
        Map<String, Object> payloadMap;
        try {
            payloadMap = ObjectMapperUtil.readValue(payloadBytes, Map.class);
        } catch (Exception ex) {
            log.error("解析服务响应 payload 失败，topic={}，payload={}",
                    topic, new String(payloadBytes, StandardCharsets.UTF_8), ex);
            return MqttDispatchResult.builder().msgId("").matched(false).build();
        }

        if (payloadMap == null) {
            log.error("服务响应 payload 为空，topic={}", topic);
            return MqttDispatchResult.builder().msgId("").matched(false).build();
        }

        // 提取 msgId
        Object msgIdValue = payloadMap.get("msgId");
        if (!(msgIdValue instanceof String msgId) || !StringUtils.hasText(msgId)) {
            log.warn("服务响应 msgId 必须是非空字符串，topic={}", topic);
            return MqttDispatchResult.builder().msgId("").matched(false).build();
        }

        Long timestamp = toPositiveLong(payloadMap.get("timestamp"));
        if (timestamp == null) {
            log.warn("服务响应 timestamp 必须是正整数，msgId={}，topic={}", msgId, topic);
            return MqttDispatchResult.builder().msgId(msgId).matched(false).build();
        }
        long clockSkew = Math.abs(System.currentTimeMillis() - timestamp);
        if (clockSkew > MAX_CLOCK_SKEW_MILLIS) {
            log.warn("服务响应时间偏差超过 5 分钟，msgId={}，timestamp={}，偏差毫秒={}，topic={}",
                    msgId, timestamp, clockSkew, topic);
        }

        Integer code = toInteger(payloadMap.get("code"));
        if (code == null) {
            log.warn("服务响应 code 必须是整数，msgId={}，topic={}", msgId, topic);
            return MqttDispatchResult.builder().msgId(msgId).matched(false).build();
        }

        Object messageValue = payloadMap.get("message");
        if (messageValue != null && !(messageValue instanceof String)) {
            log.warn("服务响应 message 必须是字符串，msgId={}，topic={}", msgId, topic);
            return MqttDispatchResult.builder().msgId(msgId).matched(false).build();
        }
        String messageText = messageValue == null
                ? (code == 20000 ? "" : "设备处理失败")
                : (String) messageValue;

        Map<String, Object> data = toMap(payloadMap.get("data"));
        if (data == null) {
            log.warn("服务响应 data 必须是 JSON Object，msgId={}，topic={}", msgId, topic);
            return MqttDispatchResult.builder().msgId(msgId).matched(false).build();
        }

        ServiceResponseMessage.ServiceResponseMessageBuilder msgBuilder = ServiceResponseMessage.builder()
                .msgId(msgId)
                .code(code)
                .message(messageText)
                .data(data)
                .timestamp(timestamp);

        // 网关子设备：从 payload 提取 target 并校验
        if ("GATEWAY_SUB_DEVICE".equals(metadata.getAccessType())) {
            Object targetObj = payloadMap.get("target");
            if (!(targetObj instanceof Map)) {
                log.warn("网关服务响应缺少 target，msgId={}，topic={}", msgId, topic);
                return MqttDispatchResult.builder().msgId(msgId).matched(false).build();
            }
            Map<String, Object> targetMap = (Map<String, Object>) targetObj;
            Object productKeyValue = targetMap.get("productKey");
            Object deviceCodeValue = targetMap.get("deviceCode");
            if (!(productKeyValue instanceof String targetProductKey)
                    || !(deviceCodeValue instanceof String targetDeviceCode)
                    || !StringUtils.hasText(targetProductKey)
                    || !StringUtils.hasText(targetDeviceCode)) {
                log.warn("网关服务响应 target 的 productKey 和 deviceCode 必须是非空字符串，msgId={}，topic={}",
                        msgId, topic);
                return MqttDispatchResult.builder().msgId(msgId).matched(false).build();
            }
            if (!MqttTopicResolver.isValidTopicVar(targetProductKey)
                    || !MqttTopicResolver.isValidTopicVar(targetDeviceCode)) {
                log.warn("网关服务响应 target 身份格式不合法，msgId={}，productKey={}，deviceCode={}，topic={}",
                        msgId, targetProductKey, targetDeviceCode, topic);
                return MqttDispatchResult.builder().msgId(msgId).matched(false).build();
            }
            msgBuilder.target(ServiceResponseMessage.TargetInfo.builder()
                    .productKey(targetProductKey)
                    .deviceCode(targetDeviceCode)
                    .build());
        }

        ServiceResponseMessage message = msgBuilder.build();

        boolean matched = serviceResponseHandler.handle(metadata, message);
        return MqttDispatchResult.builder().msgId(msgId).matched(matched).build();
    }

    private static Integer toInteger(Object obj) {
        if (!(obj instanceof Number number)) {
            return null;
        }
        long value = number.longValue();
        if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE
                || Double.compare(number.doubleValue(), (double) value) != 0) {
            return null;
        }
        return (int) value;
    }

    private static Long toPositiveLong(Object obj) {
        if (!(obj instanceof Number number)) {
            return null;
        }
        long value = number.longValue();
        if (value <= 0 || Double.compare(number.doubleValue(), (double) value) != 0) {
            return null;
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> toMap(Object obj) {
        if (obj == null) {
            return new HashMap<>();
        }
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        return null;
    }
}
