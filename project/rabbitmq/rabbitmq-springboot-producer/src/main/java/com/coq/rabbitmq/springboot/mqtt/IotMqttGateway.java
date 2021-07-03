package com.coq.rabbitmq.springboot.mqtt;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;

/**
 * @author xiaofu
 * @description rabbitmq mqtt协议网关接口
 * @date 2020/6/8 18:26
 */
@MessagingGateway(defaultRequestChannel = "iotMqttInputChannel")
public interface IotMqttGateway {

    // 向默认的 topic 发送消息
    void sendMessage2Mqtt(String payload);
    // 向指定的 topic 发送消息
    void sendMessage2Mqtt(String payload,@Header(MqttHeaders.TOPIC) String topic);
    // 向指定的 topic 发送消息，并指定服务质量参数
    void sendMessage2Mqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}


