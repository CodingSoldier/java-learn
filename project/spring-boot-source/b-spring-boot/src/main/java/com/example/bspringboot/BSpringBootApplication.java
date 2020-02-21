package com.example.bspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.bspringboot.mapper")
public class BSpringBootApplication {

    public static void main(String[] args) {

        SpringApplication.run(BSpringBootApplication.class, args);

        // 硬编码添加系统初始化器
        //SpringApplication springApplication = new SpringApplication(BSpringBootApplication.class);
        //springApplication.addInitializers(new SecondInitializer());
        //springApplication.run(args);

        //// 硬编码方式添加监听器
        //SpringApplication springApplication = new SpringApplication(BSpringBootApplication.class);
        //springApplication.addListeners(new B_Listener());
        //springApplication.run(args);

        //// 硬编码方式修改banner
        //SpringApplication springApplication = new SpringApplication(BSpringBootApplication.class);
        //springApplication.setBanner(new ResourceBanner(new ClassPathResource("banner2.txt")));
        //springApplication.run(args);

    }

}
