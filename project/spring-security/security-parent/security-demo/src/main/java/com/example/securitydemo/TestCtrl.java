package com.example.securitycore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestCtrl {

    @GetMapping("/test01")
    public Object test(){
        return "返回结果";
    }

}
