package com.example.ee2view.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class HelloWorldController {

    // 返回jsp
    @RequestMapping("")
    public String index2(@RequestHeader("Accept-Language") String acceptLanguage,
                         @CookieValue("JSESSIONID") String jsessionId,
                         Model model) {
        System.out.println(new ResponseEntity<Map>(HttpStatus.OK));
        model.addAttribute("acceptLanguage",acceptLanguage);
        model.addAttribute("jsessionId",jsessionId);
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
