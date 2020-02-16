package com.example.bspringboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.bspringboot.mapper")
public class BSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BSpringBootApplication.class, args);

        //SpringApplication springApplication = new SpringApplication(BSpringBootApplication.class);
        //springApplication.addInitializers(new SecondInitializer());
        //springApplication.run(args);
    }

}
