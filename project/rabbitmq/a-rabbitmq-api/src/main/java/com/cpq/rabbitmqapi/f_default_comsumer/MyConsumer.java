package com.cpq.rabbitmqapi.f_default_comsumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * 自定义消费者
 */
public class MyConsumer extends DefaultConsumer {

    //一定要覆盖父类方法
    public MyConsumer(Channel channel) {
        super(channel);
    }

    //消费者接受到消息，此方法会执行
    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.err.println("-----------consume message----------");
        System.err.println("consumerTag: " + consumerTag);
        System.err.println("envelope: " + envelope);
        System.err.println("properties: " + properties);
        System.err.println("body: " + new String(body));
    }


}
