package com.example.iot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IoT 系统应用入口。
 */
@SpringBootApplication
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
