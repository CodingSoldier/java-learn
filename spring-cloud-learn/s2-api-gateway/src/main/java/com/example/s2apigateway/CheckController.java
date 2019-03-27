package com.example.s2apigateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-03-27
 */
@RestController
public class CheckController {
    @GetMapping("check")
    public String check(){
        System.out.println("**************ok1111");

        return "ok1";
    }
}
