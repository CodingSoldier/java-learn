package com.example.iot.config;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * MQTT 客户端连接配置。
 */
@Data
@Component
@ConfigurationProperties(prefix = "iot.mqtt")
public class MqttProperties {

    /**
     * MQTT 服务地址。
     */
    private String host = "192.168.1.221";

    /**
     * MQTT TCP 端口。
     */
    private int port = 1883;

    /**
     * MQTT 客户端 ID。
     */
    private String clientId = "iot-service";

    /**
     * MQTT 用户名，空表示匿名连接。
     */
    private String username = "";

    /**
     * MQTT 密码，空表示匿名连接。
     */
    private String password = "";

    /**
     * 发布和订阅使用的 QoS 等级。
     */
    private int qos = 1;

    /**
     * MQTT keep alive 时间。
     */
    private Duration keepAlive = Duration.ofSeconds(60);

    /**
     * 初次连接超时时间。
     */
    private Duration connectTimeout = Duration.ofSeconds(10);
}
