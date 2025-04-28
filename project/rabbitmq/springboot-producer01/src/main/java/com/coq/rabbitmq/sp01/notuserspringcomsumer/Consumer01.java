package com.coq.rabbitmq.sp01.notuserspringcomsumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class Consumer01 extends DefaultConsumer {


    private Channel channel;

    public Consumer01(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.err.println("-----------consume message-11111---------body: " + new String(body));
        channel.basicAck(envelope.getDeliveryTag(), false);
    }


}
