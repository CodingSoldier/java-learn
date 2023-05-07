package com.example.thingdemo.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author cpq
 * @since 2023-04-11
 * mq连接工厂
 */
@Slf4j
@Configuration
public class MqttProviderConfig {

    @Autowired
    private MqttProperties mqttProperties;

    /**
     * 客户端对象
     */
    private MqttClient client;

    /**
     * 在bean初始化后连接到服务器
     */
    @PostConstruct
    public void init() {
        connect();
    }

    public void connect() {
        try {
            //创建MQTT客户端对象
            client = new MqttClient(mqttProperties.getHostUrl(), mqttProperties.getProviderClientId(), new MemoryPersistence());
            //连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            //是否清空session，设置为false表示服务器会保留客户端的连接记录（订阅主题，qos），客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            //设置为true表示每次连接到服务端都是以新的身份
            options.setCleanSession(true);
            //设置连接用户名
            options.setUserName(mqttProperties.getUsername());
            //设置连接密码
            options.setPassword(mqttProperties.getPassword().toCharArray());
            //设置超时时间，单位为秒
            options.setConnectionTimeout(100);
            //设置心跳时间 单位为秒，表示服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(20);
            //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (mqttProperties.getProviderClientId() + "与服务器断开连接").getBytes(), 0, false);
            //设置回调
            client.setCallback(new MqttProviderCallBack());
            client.connect(options);
        } catch (MqttException e) {
            log.info("创建mqtt发布者异常", e);
        }
    }

    public void publish(int qos, String topic, String message) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(qos);
        mqttMessage.setRetained(false);
        mqttMessage.setPayload(message.getBytes());
        //主题目的地，用于发布/订阅消息
        MqttTopic mqttTopic = client.getTopic(topic);
        //提供一种机制来跟踪消息的传递进度。
        //用于在以非阻塞方式（在后台运行）执行发布时跟踪消息的传递进度
        MqttDeliveryToken token;
        try {
            //将指定消息发布到主题，但不等待消息传递完成。返回的token可用于跟踪消息的传递状态。
            //一旦此方法干净地返回，消息就已被客户端接受发布。当连接可用时，将在后台完成消息传递。
            token = mqttTopic.publish(mqttMessage);
            token.waitForCompletion();
        } catch (MqttException e) {
            log.info("发送mqtt消息异常", e);
        }
    }

}
