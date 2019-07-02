package com.cpq.app02;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SpringCloudApplication
@RestController
public class App02Application {

    public static void main(String[] args) {
        SpringApplication.run(App02Application.class, args);
    }

    @GetMapping("/test02/get/{id}")
    public String test02Get(@PathVariable("id") Integer id){
        return "test02/get/"+id;
    }

    @PostMapping("/test02/post")
    public Map<String, String> test02Post(@RequestBody Map<String, String> map){
        map.put("app", "app02");
        return map;
    }

}
