package com.example.iot.mqtt;

import com.example.iot.model.MqttInvokeMessage;

/**
 * 向 MQTT 发送调用消息的网关抽象。
 */
public interface MqttGateway {

    /**
     * 向 MQTT 调用主题发送调用消息。
     *
     * @param message 调用消息
     */
    void sendInvoke(MqttInvokeMessage message);
}
