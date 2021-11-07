package com.cpq.rabbitmqapi.d_confirm_listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;

public class Producer {


    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.40.129");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //指定我们的消息投递模式: 消息的确认模式
        channel.confirmSelect();

        String exchangeName = "test_confirm_exchange";
        String routingKey = "d_confirm_listener.save";

        //5 发送一条消息
        String msg = "Hello RabbitMQ Send d_confirm_listener message!";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

        //6 添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("-------no ack!-----------");
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.err.println("-------ack!-----------");
            }
        });

    }
}
