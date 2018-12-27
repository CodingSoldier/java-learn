package com.example.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableAutoConfiguration
@MapperScan("com.example.demo.*.mapper")
@EnableFeignClients
public class MybatisDemo2Application {

	public static void main(String[] args) {
		SpringApplication.run(MybatisDemo2Application.class, args);
	}

}
