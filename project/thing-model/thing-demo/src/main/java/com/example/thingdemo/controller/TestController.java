package com.example.thingdemo.controller;

import com.example.thingdemo.cache.ThingCache;
import com.example.thingdemo.common.Result;
import com.example.thingdemo.mqtt.MqttProviderConfig;
import com.example.thingdemo.mqtt.MqttSender;
import com.example.thingdemo.mqtt.protocol.ThingResp;
import com.example.thingdemo.service.ThingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private MqttProviderConfig providerClient;
    @Autowired
    private ThingService thingService;
    @Autowired
    private MqttSender mqttSender;

    @GetMapping("/mqtt/sendMessage")
    public Result sendMessage(int qos, String topic, String message) {
        providerClient.publish(qos, topic, message);
        return Result.success();
    }

    @GetMapping("/thing/cache")
    public Result<ThingCache> cache(String productKey) {
        return Result.success(thingService.getThingCache(productKey));
    }

    @GetMapping("/thing/property/post/reply")
    public Result propertyPostReply() {
        ThingResp thingResp = new ThingResp();
        thingResp.setId("2142343");
        thingResp.setCode("200");
        thingResp.setVersion("1.0.0");
        mqttSender.propertyPostReply("83d011f5ac854aba9f5e09874b7dec20", "dev011", thingResp);
        return Result.success();
    }

}
