package com.cpq.rabbitmqapi.b_exchange.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer4DirectExchange {


    public static void main(String[] args) throws Exception {

        //1 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.40.129");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2 创建Connection
        Connection connection = connectionFactory.newConnection();
        //3 创建Channel
        Channel channel = connection.createChannel();
        //4 声明
        String exchangeName = "test_direct_exchange"; //
        String routingKey = "test.direct";
        //5 发送

        String msg = "Hello World RabbitMQ 4  Direct Exchange Message 111 ... ";
        channel.basicPublish(exchangeName, routingKey, null, msg.getBytes());

    }

}
