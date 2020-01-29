package com.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/test")
public class TestController {

    @Value("${spring.datasource.url}")
    String dbUrl;

    @GetMapping("/001")
    public String test01(){
        System.out.println(dbUrl);
        return dbUrl;
    }




    @GetMapping("/01")
    public String test1() throws Exception{
        TimeUnit.SECONDS.sleep(10);
        return "test1";
    }
    @GetMapping("/02")
    public String test2() throws Exception{
        TimeUnit.SECONDS.sleep(10);
        return "test2";
    }
    @GetMapping("/03")
    public String test3() throws Exception{
        TimeUnit.SECONDS.sleep(10);
        return "test3";
    }
    @GetMapping("/04")
    public String test4() throws Exception{
        TimeUnit.SECONDS.sleep(10);
        return "test4";
    }
    @GetMapping("/05")
    public String test5() throws Exception{
        TimeUnit.SECONDS.sleep(10);
        return "test5";
    }
    @GetMapping("/06")
    public String test6() throws Exception{
        TimeUnit.SECONDS.sleep(10);
        return "test6";
    }
    @GetMapping("/07")
    public String test7() throws Exception{
        TimeUnit.SECONDS.sleep(10);
        return "test7";
    }
    @GetMapping("/08")
    public String test8() throws Exception{
        TimeUnit.SECONDS.sleep(10);
        return "test8";
    }



}