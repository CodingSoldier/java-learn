package com.example.iot.mqtt;

import com.example.iot.model.MqttInvokeMessage;
import java.util.concurrent.CompletableFuture;

/**
 * 向 MQTT 发送调用消息的网关抽象。
 */
public interface MqttGateway {

    /**
     * 向 MQTT 调用主题发送调用消息。
     *
     * @param message 调用消息
     * @return 发送完成结果
     */
    CompletableFuture<Void> sendInvoke(MqttInvokeMessage message);
}
