package com.example.mqttclientpaho.client;

import com.example.mqttclientpaho.enums.QosEnum;
import com.example.mqttclientpaho.properties.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * mqtt客户端封装
 */
@Slf4j
@Component
public class EmqClient {

    private MqttClient mqttClient;

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttCallback mqttCallback;

    @PostConstruct
    public void init() {
        // MqttClientPersistence是接口，实现类有 MqttDefaultFilePersistence、MemoryPersistence
        MemoryPersistence memoryPersistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(mqttProperties.getBrokerUrl(), mqttProperties.getClientId(), memoryPersistence);
        } catch (MqttException e) {
            log.error("初始化mqttClient异常", e);
        }
    }

    /**
     * 连接broker
     */
    public void connect(String username, String password) {
        // 创建 Mqtt连接选项
        MqttConnectOptions connectOptions = new MqttConnectOptions();
        // 自动重连
        connectOptions.setAutomaticReconnect(true);
        /**
         * 设置为true后意味着：客户端断开连接后emq不保留会话保留会话，否则会产生订阅共享队列的存活
         客户端收不到消息的情况
         * 因为断开的连接还被保留的话，emq会将队列中的消息负载到断开但还保留的客户端，导致存活的客户
         端收不到消息
         * 解决该问题有两种方案:1.连接断开后不要保持；2.保证每个客户端有固定的clientId
         */
        connectOptions.setCleanSession(true);
        connectOptions.setUserName(username);
        connectOptions.setPassword(password.toCharArray());

        //设置mqtt消息回调
        mqttClient.setCallback(mqttCallback);

        try {
            mqttClient.connect(connectOptions);
        } catch (MqttException e) {
            log.error("连接mqtt broker失败", e);
        }
    }

    /**
     * 发布消息
     */
    public void publish(String topic, String payload, QosEnum qos, boolean retain) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos.value());
        mqttMessage.setRetained(retain);
        mqttMessage.setPayload(payload.getBytes());
        if (mqttClient.isConnected()) {
            try {
                mqttClient.publish(topic, mqttMessage);
            } catch (MqttException e) {
                log.error("发布消息失败", e);
            }
        }
    }

    /**
     * 订阅主题
     */
    public void subscribe(String topicFilter, QosEnum qos) {
        try {
            mqttClient.subscribe(topicFilter, qos.value());
        } catch (MqttException e) {
            log.error("订阅失败", e);
        }
    }

    /**
     * 取消订阅
     */
    public void unSubscribe(String topicFilter) {
        try {
            mqttClient.unsubscribe(topicFilter);
        } catch (MqttException e) {
            log.error("取消订阅异常", e);
        }
    }

    /**
     * 重连
     */
    public void reConnect() {
        try {
            mqttClient.reconnect();
        } catch (MqttException e) {
            log.error("重连异常", e);
        }
    }

    @PreDestroy
    public void disConnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            log.error("断开连接异常", e);
        }
    }

}
