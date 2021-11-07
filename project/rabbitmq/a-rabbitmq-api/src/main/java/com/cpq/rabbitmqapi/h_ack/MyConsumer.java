package com.cpq.rabbitmqapi.h_ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MyConsumer extends DefaultConsumer {


    private Channel channel;

    public MyConsumer(Channel channel) {
        super(channel);
        this.channel = channel;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        System.err.println("-----------consume message----------");
        System.err.println("body: " + new String(body));
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //如果是0则Nack不签收，并把消息返回给生产者（重回队列）
        if ((Integer) properties.getHeaders().get("num") == 0) {
            /**
             basicNack(long deliveryTag, boolean multiple, boolean requeue)，其中deliveryTag 可以看作消息的编号，它是一个64 位的长整型值。multiple一般设为false，如果设为true则表示拒绝当前deliveryTag 编号及之前所有未被当前消费者确认的消息。requeue参数表示是否重回队列，如果requeue 参数设置为true ，则RabbitMQ 会重新将这条消息存入队列尾部（注意是队列尾部），等待继续投递给订阅该队列的消费者，当然也可能是自己；如果requeue 参数设置为false ，则RabbitMQ立即会把消息从队列中移除，而不会把它发送给新的消费者。
             */
            channel.basicNack(envelope.getDeliveryTag(), false, true);
        } else {
            //签收
            channel.basicAck(envelope.getDeliveryTag(), false);
        }

    }


}
