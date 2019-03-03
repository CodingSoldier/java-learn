package com.cpq.rabbitmqapi.c_message;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Procuder {

	
	public static void main(String[] args) throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.40.129");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		
		Channel channel = connection.createChannel();
		
		Map<String, Object> headers = new HashMap<>();
		headers.put("my1", "111");
		headers.put("my2", "222");

		//设置properties
		AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
				//1非持久化，消费者没收到消息前重启rabbitmq，重启后消费者不能收到消息
				//2持久化，重启后能收到消息
				.deliveryMode(2)
				.contentEncoding("UTF-8")
				.expiration("10000")  //消息过期时间，10秒后消费者没收到消息，消息将被丢弃
				.headers(headers)  //头部自定义信息
				.build();  //链式调用，以build()结尾表明调用完毕。
		
		for(int i=0; i < 5; i++){
			String msg = "Hello RabbitMQ!";
			channel.basicPublish("", "test001", properties, msg.getBytes());
		}

		channel.close();
		connection.close();
	}
}
