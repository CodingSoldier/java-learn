package com.cpq.docker1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Docker1Application {

    public static void main(String[] args) {
        SpringApplication.run(Docker1Application.class, args);
    }

    @GetMapping("docker1/get")
    public String docker1Get(){
        return "docker1/get";
    }

}
