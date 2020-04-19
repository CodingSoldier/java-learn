package com.example.springbootdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.util.Properties;

@PropertySource("classpath:application-property-source.properties")
@SpringBootApplication
public class SpringBootDemoApplication {
    public static void main(String[] args) {

        System.setProperty("test.property", "9、Java系统属性（System.getProperties()）");

        SpringApplication app = new SpringApplication(SpringBootDemoApplication.class);
        Properties properties = new Properties();
        properties.setProperty("test.property", "17、默认属性（通过设置SpringApplication.setDefaultProperties指定）");
        app.setDefaultProperties(properties);
        app.run(args);
    }
}
