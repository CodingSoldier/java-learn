package com.cpq.app01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@SpringCloudApplication
@EnableFeignClients
@RestController
public class App01Application {

    @Autowired
    App02Feign app02Feign;

    public static void main(String[] args) {
        SpringApplication.run(App01Application.class, args);
    }

    @GetMapping("/test01/get")
    public String test01Get(){
        System.out.println("/test01/get");
        return "test01/get";
    }

    @PostMapping("/test01/post")
    public Object test01Post(@RequestBody Map<String, String> map){
        System.out.println(map.toString());
        map.put("app", "app01");
        return map;
    }

    @PostMapping("/test01/test02/post")
    public Object test0102Post(@RequestBody Map<String, String> map){
        System.out.println(map.toString());
        map.put("feign", "feign");
        return app02Feign.test02Post(map);
    }

}
