package com.example.thingdemo.controller;

import com.example.thingdemo.common.Result;
import com.example.thingdemo.mqtt.MqttProviderConfig;
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

    @RequestMapping("/mqtt/sendMessage")
    public Result sendMessage(int qos, String topic, String message){
        providerClient.publish(qos,topic, message);
        return Result.success();
    }
}
