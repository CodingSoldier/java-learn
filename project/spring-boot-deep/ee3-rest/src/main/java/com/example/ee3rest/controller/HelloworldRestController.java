package com.example.ee3rest.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenpiqian
 * @date: 2020-03-12
 */
@RestController
public class HelloworldRestController {

    @GetMapping(value = "/hello-world")
    public String helloWorld() {
        return "Hello,World! : ";
    }

}
