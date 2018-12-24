package com.cpq.txapp02;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@EnableAutoConfiguration
@SpringBootApplication
@EnableEurekaClient
@MapperScan("com.cpq.txapp02.*.mapper")
public class Application {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplateBalanced(){
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
