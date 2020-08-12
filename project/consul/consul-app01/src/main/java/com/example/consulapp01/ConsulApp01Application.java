package com.example.consulapp01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ConsulApp01Application {

    public static void main(String[] args) {
        SpringApplication.run(ConsulApp01Application.class, args);
    }

}
