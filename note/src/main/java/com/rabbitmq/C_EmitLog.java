package com.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class C_EmitLog {

    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        for (int i=0; i<5; i++){
            String message = "Hello World!"+i;
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
            System.out.println("Sent  "+message);
        }
        channel.close();
        connection.close();
    }

}
