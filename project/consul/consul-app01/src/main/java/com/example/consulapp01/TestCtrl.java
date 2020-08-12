package com.example.consulapp01;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenpiqian
 * @date: 2020-08-11
 */
@RestController
public class TestCtrl {

    @RequestMapping("/test01")
    public String home() {
        return "Hello world";
    }

}
