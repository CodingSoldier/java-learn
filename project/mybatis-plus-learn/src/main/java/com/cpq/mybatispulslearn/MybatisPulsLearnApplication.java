package com.cpq.mybatispulslearn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
//@MapperScan("com.cpq.mybatispulslearn.**.mapper")
public class MybatisPulsLearnApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisPulsLearnApplication.class, args);
    }

}

