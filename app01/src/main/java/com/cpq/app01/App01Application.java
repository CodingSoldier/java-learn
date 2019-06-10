package com.cpq.app01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App01Application {

    public static void main(String[] args) {
        SpringApplication.run(App01Application.class, args);
    }

    @GetMapping("app01/get")
    public String app01Get(){
        return "app01/get";
    }

}
