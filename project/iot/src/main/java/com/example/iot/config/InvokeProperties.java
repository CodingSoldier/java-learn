package com.example.iot.config;

import java.time.Duration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 服务调用请求相关配置。
 */
@Data
@Component
@ConfigurationProperties(prefix = "iot.invoke")
public class InvokeProperties {

    /**
     * 等待匹配 MQTT 回复的最长时间。
     */
    private Duration timeout = Duration.ofSeconds(30);

    /**
     * 内存中允许保留的最大待处理调用请求数。
     */
    private int maxPending = 10_000;
}
