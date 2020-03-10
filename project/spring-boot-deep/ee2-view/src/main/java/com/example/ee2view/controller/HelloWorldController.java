package com.example.ee2view.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class HelloWorldController {

    // 返回jsp，还是得打成war包才能访问
    @RequestMapping("/index")
    public String index2(Model model) {
        System.out.println(new ResponseEntity<Map>(HttpStatus.OK));
        return "index";
    }

    // 返回thymeleaf
    @GetMapping("/hello-world")
    public String helloWorld() {
        return "hello-world"; // View 逻辑名称
    }

    @ModelAttribute("message")
    public String message() {
        return "HelloWorld";
    }

}
