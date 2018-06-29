package com.cpq.eurekaconsumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DcController {

    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RestTemplate restTemplateBalanced;

    //  http://localhost:2101/consumer
    @GetMapping("/consumer")
    public String dc(){
        ServiceInstance serviceInstance = loadBalancerClient.choose("eureka-client");
        String url = "http://"+serviceInstance.getHost()+":"+serviceInstance.getPort()+"/dc";
        System.out.println(url);

        //有负载的的RestTemplate，URL的http://被治理的服务名称/controller路径
        restTemplateBalanced.getForObject("http://eureka-client/dc",String.class);

        //没有负载的RestTemplate，URL的http://被治理的服务 或者 host:port /controller路径
        return restTemplate.getForObject(url, String.class);
    }

}
