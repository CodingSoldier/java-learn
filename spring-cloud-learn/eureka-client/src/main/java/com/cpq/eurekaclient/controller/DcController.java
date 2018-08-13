package com.cpq.eurekaclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DcController {

    @Autowired
    DiscoveryClient discoveryClient;
    @Value("${custom.time.sleep}")
    long sleepTime;

    @GetMapping("/dc")
    public String dc(HttpServletRequest request) throws Exception{

        //request.getHeader("null point exception").length();

        //Thread.sleep(5000L);  //睡眠，相当于其他服务调用时弄个错误

        //System.out.println(request.getCookies());
        System.out.println(request.getServerPort());
        String services = "Services: " + discoveryClient.getServices();
        //System.out.println(services);
        return services;
    }

    @GetMapping("/dc2")
    public String dc时(HttpServletRequest request) throws Exception{

        //request.getHeader("null point exception").length();

        System.out.println(sleepTime);
        Thread.sleep(sleepTime);  //睡眠，相当于其他服务调用时弄个错误

        //System.out.println(request.getCookies());
        System.out.println(request.getServerPort());
        String services = "Services: " + discoveryClient.getServices();
        //System.out.println(services);
        return services;
    }

}
