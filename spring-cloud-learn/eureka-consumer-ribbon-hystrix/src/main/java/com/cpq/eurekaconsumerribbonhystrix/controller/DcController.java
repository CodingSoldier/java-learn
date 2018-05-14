package com.cpq.eurekaconsumerribbonhystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class DcController {

    @Autowired
    ConsumerService consumerService;

    @GetMapping("/consumer")
    public String dc(){
        return consumerService.consumer();
    }

    @Component
    class ConsumerService {
        @Autowired
        RestTemplate restTemplate;

        @HystrixCommand(fallbackMethod = "fallback")
        public String consumer(){
            /**  getForObject方法
             * T getForObject(URI url, Class responseType)
             * T getForObject(String url, Class responseType, MapString< String, ?> urlVariables)
             * T getForObject(String url, Class responseType, Object… urlVariables)
             */
            return restTemplate.getForObject("http://eureka-client/dc", String.class);
        }

        public String fallback() {
            return "请求出错，调用降级处理函数";
        }
    }

}
