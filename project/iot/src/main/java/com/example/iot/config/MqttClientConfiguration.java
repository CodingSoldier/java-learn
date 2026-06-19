package com.example.iot.config;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * HiveMQ MQTT 客户端配置。
 */
@Configuration
public class MqttClientConfiguration {

    /**
     * 创建 MQTT 5 异步客户端。
     *
     * @param properties MQTT 配置
     * @return MQTT 5 异步客户端
     */
    @Bean
    public Mqtt5AsyncClient mqtt5AsyncClient(MqttProperties properties) {
        return MqttClient.builder()
                .useMqttVersion5()
                .identifier(properties.getClientId())
                .serverHost(properties.getHost())
                .serverPort(properties.getPort())
                .automaticReconnectWithDefaultConfig()
                .buildAsync();
    }
}
