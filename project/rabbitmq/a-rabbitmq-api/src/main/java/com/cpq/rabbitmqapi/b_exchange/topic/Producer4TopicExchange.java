package com.cpq.rabbitmqapi.b_exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer4TopicExchange {

	
	public static void main(String[] args) throws Exception {
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.40.129");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();

		Channel channel = connection.createChannel();

		String exchangeName = "test_topic_exchange";
		String routingKey1 = "user.save";
		String routingKey2 = "user.update";
		String routingKey3 = "user";
		
		String msg = "Hello World RabbitMQ 4 Topic Exchange Message ...";
		channel.basicPublish(exchangeName, routingKey1 , null , msg.getBytes()); 
		channel.basicPublish(exchangeName, routingKey2 , null , msg.getBytes()); 	
		channel.basicPublish(exchangeName, routingKey3 , null , msg.getBytes()); 
		channel.close();  
        connection.close();  
	}
	
}
