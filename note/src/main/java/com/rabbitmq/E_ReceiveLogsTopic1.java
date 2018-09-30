package com.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class E_ReceiveLogsTopic1 {
    private static final String EXCHANGE_NAME = "topic_logs";
    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();
        String[] routingKeys = new String[]{"*.orange.*"};
        for (String bindingKey:routingKeys){
            channel.queueBind(queueName, EXCHANGE_NAME, bindingKey);
            System.out.println("交换器名称："+EXCHANGE_NAME+",队列名称："+queueName+",路由规则关键字："+bindingKey);
        }
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("消息关键字："+envelope.getRoutingKey()+"\n消息："+message);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}
