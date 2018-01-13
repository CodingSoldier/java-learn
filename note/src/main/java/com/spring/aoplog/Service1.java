package com.spring.aoplog;

import org.springframework.stereotype.Service;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/13
 */
@Service
public class Service1 {
    public void say(){
        System.out.println("Service方法");
    }
}
