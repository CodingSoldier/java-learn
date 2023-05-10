package com.example.thingdemo.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author chenpq05
 * @since 2023/4/11 11:43
 * <p>
 * mqtt消费者配置
 * https://www.cnblogs.com/xct5622/p/15094017.html
 */
@Slf4j
@Configuration
public class MqttConsumerConfig {

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

    /**
     * 客户端连接服务端
     */
    public void connect() {
        try {
            //创建MQTT客户端对象
            client = new MqttClient(mqttProperties.getHostUrl(), mqttProperties.getConsumerClientId(), new MemoryPersistence());
            //连接设置
            MqttConnectOptions options = new MqttConnectOptions();
            //是否清空session，设置为false表示服务器会保留客户端的连接记录，客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
            //设置为true表示每次连接到服务端都是以新的身份
            options.setCleanSession(true);
            //设置连接用户名
            options.setUserName(mqttProperties.getUsername());
            //设置连接密码
            options.setPassword(mqttProperties.getPassword().toCharArray());
            //设置超时时间，单位为秒
            options.setConnectionTimeout(mqttProperties.getTimeout());
            //设置心跳时间 单位为秒，表示服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
            options.setKeepAliveInterval(mqttProperties.getKeepalive());
            //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (mqttProperties.getConsumerClientId() + "与服务器断开连接").getBytes(), 0, false);
            //设置回调
            client.setCallback(new MqttConsumerCallBack());
            client.connect(options);
            //订阅主题
            //消息等级，和主题数组一一对应，服务端将按照指定等级给订阅了主题的客户端推送消息
            //int[] qos = {1,1};
            // 监听所有物模型主题
            String[] topics = {"sys/+/+/thing/#"};
            //订阅主题
            client.subscribe(topics);
        } catch (MqttException e) {
            log.error("MQTT消费者创建异常", e);
        }
    }


    /**
     * 断开连接
     */
    public void disConnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            log.error("MQTT断开连接创建异常", e);
        }
    }


    /**
     * 订阅主题
     */
    public void subscribe(String topic, int qos) {
        try {
            client.subscribe(topic, qos);
        } catch (MqttException e) {
            log.error("MQTT订阅主题异常", e);
        }
    }
}
