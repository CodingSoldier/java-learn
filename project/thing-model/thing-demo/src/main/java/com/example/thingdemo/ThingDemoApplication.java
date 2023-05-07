package com.example.thingdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.example.thingdemo.mapper.**"})
public class ThingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThingDemoApplication.class, args);
    }

}
