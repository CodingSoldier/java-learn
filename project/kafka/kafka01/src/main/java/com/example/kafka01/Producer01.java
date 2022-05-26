package com.example.kafka01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Producer01 {


    @Autowired
    KafkaTemplate<String, String> kafka;

    @RequestMapping("/send01")
    public String data(@RequestParam("msg") String msg){
        // 通过kafka发送出去
        kafka.send("test01", msg);
        return "ok";
    }

}
