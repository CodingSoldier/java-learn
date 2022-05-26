package com.example.kafka01;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer01 {

    @KafkaListener(topics = "test01")
    public void consumerTopic(String msg){
        System.out.println("收到消息：" + msg);
    }

}
