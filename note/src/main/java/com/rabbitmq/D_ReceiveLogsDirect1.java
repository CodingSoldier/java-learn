package com.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class D_ReceiveLogsDirect1 {
    private static final String EXCHANGE_NAME = "direct_logs";
    private static final String[] routingKeys = new String[]{"info" ,"warning", "error"};

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName=channel.queueDeclare().getQueue();
        for (String severity:routingKeys){
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
            System.out.println("交换器名称："+EXCHANGE_NAME+"，队列名："+queueName);
        }
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("路由规则："+envelope.getRoutingKey()+"，消息："+message);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
