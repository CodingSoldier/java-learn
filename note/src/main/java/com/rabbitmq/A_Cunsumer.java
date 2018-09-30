package com.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

/*
 https://blog.csdn.net/chwshuang/article/details/50521708
先运行 A_Cunsumer
rabbitmqctl list_queues
rabbitmqctl stop_app
rabbitmqctl reset
rabbitmqctl start_app
 */
public class A_Cunsumer {

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(A_Producer.HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(A_Producer.QUEUE_NAME, false, false, false, null);
        System.out.println("等待消息~~~~~~");
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("收到消息："+message);
            }
        };
        channel.basicConsume(A_Producer.QUEUE_NAME, true, consumer);
    }

}
