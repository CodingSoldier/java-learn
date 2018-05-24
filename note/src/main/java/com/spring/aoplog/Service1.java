package com.spring.aoplog;

import org.springframework.stereotype.Service;

@Service
public class Service1 {
    public void say(){
        System.out.println("Service方法");
    }
}
