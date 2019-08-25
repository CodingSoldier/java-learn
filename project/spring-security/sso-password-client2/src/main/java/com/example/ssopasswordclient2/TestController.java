package com.example.ssopasswordclient2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/hello/client")
    public String hello(){
        return "hello-client";
    }

}
