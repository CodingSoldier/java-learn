package com.cpq.eurekaclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class DcController {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/dc")
    public String dc(HttpServletRequest request) throws Exception{

        //request.getHeader("null point exception").length();

        //Thread.sleep(5000L);  //睡眠，相当于其他服务调用时弄个错误

        //System.out.println(request.getCookies());

        String services = "Services: " + discoveryClient.getServices();
        //System.out.println(services);
        return services;
    }

}
