package com.cpq.eurekaclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DcController {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/dc")
    public String dc() throws Exception{
        //Thread.sleep(5000L);  //睡眠，相当于其他服务调用时弄个错误
        String services = "Services: " + discoveryClient.getServices();
        System.out.println(services);
        return services;
    }

}
