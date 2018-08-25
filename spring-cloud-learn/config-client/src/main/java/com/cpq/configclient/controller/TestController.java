package com.cpq.configclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/config-client")
public class TestController {

    @Value("${common.item}")
    String commonItem;
    @Value("${custom.param1}")
    String customParam1;
    //@Value("${common.eureka-consumer}")  //无法读取eureka-consumer目录下的文件
    //String commonEurekaConsumer;

    @GetMapping("/dc")
    public String dc(HttpServletRequest request) throws Exception{
        System.out.println("******* common.item: "+commonItem);
        System.out.println("******* customParam1: "+customParam1);
        return commonItem + "  " + customParam1;
    }

}
