package com.cpq.mybatispulslearn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.cpq.mybatispulslearn.*.mapper")
public class MybatisPulsLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPulsLearnApplication.class, args);
    }



}

