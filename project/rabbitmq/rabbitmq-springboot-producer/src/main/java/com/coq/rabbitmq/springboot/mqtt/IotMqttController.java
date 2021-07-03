package com.coq.rabbitmq.springboot.mqtt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * https://zhuanlan.zhihu.com/p/359455682
 * https://github.com/chengxy-nds/Springboot-Notebook/tree/master/springboot-mqtt-messagepush
 *
 * MQTT使用
 */
@Controller
@RequestMapping("/mqtt")
public class IotMqttController {

    @Autowired
    private IotMqttGateway mqttGateway;


    @RequestMapping("/sendMessage")
    public String sendMqtt(@RequestParam(value = "topic") String topic, @RequestParam(value = "message") String message) {
        mqttGateway.sendMessage2Mqtt(message, topic);
        return "SUCCESS";
    }


    @GetMapping("/send")
    public String send(@RequestParam("msg")String msg) {
        mqttGateway.sendMessage2Mqtt(msg);
        System.out.println("发送信息："+msg);
        return msg;
    }

}