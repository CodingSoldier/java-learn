package com.cpq.app002;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class App02Application {

    public static void main(String[] args) {
        SpringApplication.run(App02Application.class, args);
    }

    @GetMapping("/test02/get")
    public String test02Get(){
        return "test02/get";
    }

    @PostMapping("/test02/post")
    public Object test02Post(@RequestBody Map<String, String> map){
        map.put("app", "app002");
        return map;
    }

}
