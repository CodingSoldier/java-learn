package com.imooc.web.boostrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import java.io.File;

/**
 * Spring Boot Web MVC 引导类
 *
 * @author 小马哥
 * @since 2018/5/21
 */
@SpringBootApplication(scanBasePackages = "com.imooc.web")
public class SpringBootWebMvcBootstrap {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebMvcBootstrap.class, args);
    }

}
