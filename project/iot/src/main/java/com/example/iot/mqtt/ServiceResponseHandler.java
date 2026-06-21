package com.example.iot.mqtt;

import com.example.iot.model.MqttTopicMetadata;
import com.example.iot.model.ServiceResponseMessage;
import com.example.iot.service.PendingRequest;
import com.example.iot.service.PendingRequestRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * MQTT 服务响应处理器。
 * <p>
 * 校验响应身份与待处理请求预期身份一致后，使用完整响应完成对应的 PendingRequest。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceResponseHandler {

    private final PendingRequestRegistry pendingRequestRegistry;

    /**
     * 处理服务响应消息。
     *
     * @param metadata Topic 解析元数据
     * @param message  服务响应消息
     * @return 是否成功匹配并处理
     */
    public boolean handle(MqttTopicMetadata metadata, ServiceResponseMessage message) {
        String msgId = message.getMsgId();
        if (msgId == null || msgId.isEmpty()) {
            log.warn("服务响应缺少 msgId，topic={}，message={}", metadata, message);
            return false;
        }

        if (message.getCode() == null) {
            log.warn("服务响应缺少 code，msgId={}，topic={}", msgId, metadata);
            return false;
        }

        PendingRequest pending = pendingRequestRegistry.get(msgId);
        if (pending == null) {
            log.warn("收到未知 msgId 的服务响应，msgId={}", msgId);
            return false;
        }

        // 校验身份一致性
        if (!validateIdentity(metadata, message, pending)) {
            return false;
        }

        boolean completed = pendingRequestRegistry.complete(msgId, message);
        log.info("服务响应已完成请求，msgId={}，code={}，completed={}",
                msgId, message.getCode(), completed);
        return completed;
    }

    private boolean validateIdentity(MqttTopicMetadata metadata, ServiceResponseMessage message, PendingRequest pending) {
        String accessType = metadata.getAccessType();

        if ("DIRECT_DEVICE".equals(accessType)) {
            // 直连设备：校验 Topic 中的 productKey + deviceCode
            if (!metadata.getProductKey().equals(pending.getExpectedProductKey())
                    || !metadata.getDeviceCode().equals(pending.getExpectedDeviceCode())) {
                log.warn("直连设备服务响应身份不匹配，topic productKey={}，deviceCode={}，expected productKey={}，deviceCode={}，msgId={}",
                        metadata.getProductKey(), metadata.getDeviceCode(),
                        pending.getExpectedProductKey(), pending.getExpectedDeviceCode(),
                        message.getMsgId());
                return false;
            }
        } else if ("GATEWAY_SUB_DEVICE".equals(accessType)) {
            // 网关子设备：校验 Topic 中的 gatewayId + payload 中的 target
            if (!metadata.getGatewayId().equals(pending.getExpectedGatewayId())) {
                log.warn("网关服务响应 gatewayId 不匹配，topic gatewayId={}，expected={}，msgId={}",
                        metadata.getGatewayId(), pending.getExpectedGatewayId(), message.getMsgId());
                return false;
            }
            // 校验 target 中的子设备身份
            ServiceResponseMessage.TargetInfo target = message.getTarget();
            if (target == null || target.getProductKey() == null || target.getDeviceCode() == null) {
                log.warn("网关服务响应 target 不完整，msgId={}", message.getMsgId());
                return false;
            }
            if (!target.getProductKey().equals(pending.getExpectedProductKey())
                    || !target.getDeviceCode().equals(pending.getExpectedDeviceCode())) {
                log.warn("网关服务响应 target 子设备身份不匹配，target productKey={}，deviceCode={}，expected productKey={}，deviceCode={}，msgId={}",
                        target.getProductKey(), target.getDeviceCode(),
                        pending.getExpectedProductKey(), pending.getExpectedDeviceCode(),
                        message.getMsgId());
                return false;
            }
        }

        // 校验 serviceCode
        if (metadata.getServiceCode() != null && !metadata.getServiceCode().isEmpty()
                && pending.getExpectedServiceCode() != null
                && !metadata.getServiceCode().equals(pending.getExpectedServiceCode())) {
            log.warn("服务响应 serviceCode 不匹配，topic serviceCode={}，expected={}，msgId={}",
                    metadata.getServiceCode(), pending.getExpectedServiceCode(), message.getMsgId());
            return false;
        }

        return true;
    }
}
