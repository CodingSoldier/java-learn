package com.example.mqttclientpaho;

import com.example.mqttclientpaho.client.EmqClient;
import com.example.mqttclientpaho.enums.QosEnum;
import com.example.mqttclientpaho.properties.MqttProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TestMqtt implements CommandLineRunner {
    @Autowired
    private EmqClient emqClient;
    @Autowired
    private MqttProperties mqttProperties;

    @Override
    public void run(String... args) throws Exception {
        // 连接mqtt broker
        emqClient.connect(mqttProperties.getUsername(), mqttProperties.getPassword());
        // 订阅主题
        emqClient.subscribe("testtopic/#", QosEnum.QoS2);

        // 循环推送消息测试
        new Thread(() -> {
            String topic = "testtopic/1234";
            while (true) {
                String payload = "循环推送消息" + LocalDateTime.now().toString();
                emqClient.publish(topic, payload, QosEnum.QoS2, false);
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("", e);
                }
            }
        }).start();
    }

}
