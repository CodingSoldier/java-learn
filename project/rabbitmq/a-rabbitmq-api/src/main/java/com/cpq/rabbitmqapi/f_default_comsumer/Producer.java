package com.cpq.rabbitmqapi.f_default_comsumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {


    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        String exchange = "test_consumer_exchange";
        String routingKey = "consumer.save";

        String msg = "Hello RabbitMQ Consumer Message";

        for (int i = 0; i < 5; i++) {
            channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());
        }

    }

}
