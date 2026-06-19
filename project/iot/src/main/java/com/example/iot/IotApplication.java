package com.example.iot;

import com.example.iot.config.InvokeProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * IoT 系统应用入口。
 */
@SpringBootApplication
@EnableConfigurationProperties(InvokeProperties.class)
public class IotApplication {

    /**
     * 启动 IoT Spring Boot 应用。
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(IotApplication.class, args);
    }
}
