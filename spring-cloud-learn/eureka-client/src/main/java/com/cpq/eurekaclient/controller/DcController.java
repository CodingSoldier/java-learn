package com.cpq.eurekaclient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    @GetMapping("/map")
    public Map map(HttpServletRequest request) throws Exception{

        Map map = new HashMap();
        map.put("status", 0);
        map.put("data", 1234546);
        return map;
    }

    @PostMapping("/post/map")
    public Map pmap(@RequestBody Map<String, String> param) throws Exception{

        Map map = new HashMap();
        map.put("status", 100);
        map.put("data", "data");
        return map;
    }

    @GetMapping("/dc2")
    public String dc2(HttpServletRequest request) throws Exception{

        //request.getHeader("null point exception").length();

        //System.out.println(sleepTime);
        //Thread.sleep(sleepTime);  //睡眠，相当于其他服务调用时弄个错误

        //System.out.println(request.getCookies());
        System.out.println(request.getServerPort());
        String services = "Services: " + discoveryClient.getServices();
        //System.out.println(services);
        return services;
    }

    @GetMapping("/dc3")
    public String dc3(HttpServletRequest request) throws Exception{
        System.out.println(new Date() +"  port: " +request.getServerPort());
        //TimeUnit.SECONDS.sleep(60000);
        System.out.println(request.getServerPort());
        String services = "Services: " + discoveryClient.getServices();
        return services;
    }

    @PostMapping("/p4")
    public String p4(HttpServletRequest request) throws Exception{
        System.out.println(new Date() +"   port: " +request.getServerPort());
        //TimeUnit.SECONDS.sleep(60000);
        System.out.println(request.getServerPort());
        String services = "Services: " + discoveryClient.getServices();
        return services;
    }

}
