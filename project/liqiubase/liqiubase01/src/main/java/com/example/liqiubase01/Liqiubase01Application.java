package com.example.liqiubase01;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.liqiubase01.test01.mapper")
public class Liqiubase01Application {

    public static void main(String[] args) {
        SpringApplication.run(Liqiubase01Application.class, args);
    }

}
