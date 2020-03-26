package com.example.cpq.dspringmvcauto.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class HelloWorldController {

    // http://localhost:8080/
    @RequestMapping("")
    public String index(Model model) {
        System.out.println(new ResponseEntity<Map>(HttpStatus.OK));
        model.addAttribute("message","Hello,World");
        return "index";
    }

    // http://localhost:8080/index2
    @RequestMapping("/index2")
    public String index2(@RequestHeader("Accept-Language") String acceptLanguage,
                         @CookieValue("JSESSIONID") String jsessionId,
                         Model model) {
        System.out.println(new ResponseEntity<Map>(HttpStatus.OK));
        model.addAttribute("acceptLanguage",acceptLanguage);
        model.addAttribute("jsessionId",jsessionId);
        return "index";
    }

    // 这么写，message也会注入所有model的属性中，jsp能拿到message属性
    @ModelAttribute("message")
    public String message(){
        return "Hello, World";
    }


    /**
     * http://localhost:8080/error?num=fsd
     * 发生异常后，会走onException
     */
    @RequestMapping("/error")
    public String error(@RequestParam int num) {
        return "index";
    }
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> onException(Throwable throwable){
        // 发生异常，仍然给前端返回ResponseEntity.status=200
        return ResponseEntity.ok(throwable.getMessage());
    }

}
