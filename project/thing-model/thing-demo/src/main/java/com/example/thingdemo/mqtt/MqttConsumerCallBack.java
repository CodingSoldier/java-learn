package com.example.thingdemo.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

/**
 * @author chenpq05
 * @since 2023/4/11 11:45
 *
 * mqtt消费者回调
 */
@Slf4j
public class MqttConsumerCallBack implements MqttCallback {

  /**
   * 客户端断开连接的回调
   */
  @Override
  public void connectionLost(Throwable throwable) {
    log.info("与服务器断开连接", throwable);
  }

  /**
   * 消息到达的回调
   */
  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
    log.info("接收消息主题={}，Qos={}， 消息retained={}", topic, message.getQos(), message.isRetained());

    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
    log.info("接收消息内容={}", payload);
  }

  /**
   * 消息发布成功回调
   * @param iMqttDeliveryToken
   */
  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    log.info("接收消息成功");
  }

}
