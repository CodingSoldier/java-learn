package com.coq.rabbitmq.sp01.producer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MyConsumer2 extends DefaultConsumer {


    private Channel channel;

    public MyConsumer2(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.err.println("-----------consume message-222222222222---------body: " + new String(body));
        channel.basicAck(envelope.getDeliveryTag(), false);
    }


}