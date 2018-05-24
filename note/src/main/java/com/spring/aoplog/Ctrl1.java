package com.spring.aoplog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class Ctrl1 {

    @Autowired
    private Service1 service1;

    /**
     * 使用@Log注解的testLog方法可以被切入信息，
     * Service中的say方法没有添加@Log注解，不会被切入信息
     */
    @Log
    public void testLog(){
        System.out.println("Ctrl1使用@Log注解");
        service1.say();
    }

    public void testNoLog(){
        System.out.println("没有@Log注解");
    }
}
