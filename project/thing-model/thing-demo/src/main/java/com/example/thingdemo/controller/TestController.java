package com.example.thingdemo.controller;

import com.example.thingdemo.cache.TingCache;
import com.example.thingdemo.common.Result;
import com.example.thingdemo.mqtt.MqttProviderConfig;
import com.example.thingdemo.service.TingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MqttProviderConfig providerClient;
    @Autowired
    private TingService tingService;

    @RequestMapping("/mqtt/sendMessage")
    public Result sendMessage(int qos, String topic, String message) {
        providerClient.publish(qos, topic, message);
        return Result.success();
    }

    @RequestMapping("/ting/cache")
    public Result<TingCache> cache(String productKey) {
        return Result.success(tingService.getTingCache(productKey));
    }

}
