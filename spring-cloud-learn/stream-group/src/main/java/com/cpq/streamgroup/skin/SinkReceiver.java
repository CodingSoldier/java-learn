package com.cpq.streamgroup.skin;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(value = {Sink.class})
public class SinkReceiver {

    //定时发送消息给group01
    @StreamListener(Sink.INPUT)
    public void reverve(User user){
        System.out.println("streamgroup.skin.SinkReceiver接收： "+user.toString());
    }

}
