package com.cpq.eurekaconsumer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/eureka-consumer")
public class TestController {

    @Value("${common.item}")
    String commonItem;
    @Value("${common.eureka-consumer}")
    String commonEurekaConsumer;
    @Value("${custom.param1}")
    String customParam1;

    @GetMapping("/dc")
    public String dc(HttpServletRequest request) throws Exception{
        System.out.println("******* common.item: "+commonItem);
        System.out.println("******* customParam1: "+customParam1);
        System.out.println("******* commonEurekaConsumer: "+commonEurekaConsumer);
        return commonItem + "  " + customParam1 + "  " + commonEurekaConsumer;
    }

}
