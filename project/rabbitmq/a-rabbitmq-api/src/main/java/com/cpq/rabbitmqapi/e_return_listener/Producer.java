package com.cpq.rabbitmqapi.e_return_listener;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnListener;

import java.io.IOException;

public class Producer {

	
	public static void main(String[] args) throws Exception {
		
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("localhost");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchange = "test_return_exchange";
		String routingKey = "return.save";
		String routingKeyError = "abc.save";
		
		String msg = "Hello RabbitMQ Return Message";

		//第三个参数immediate为true表示交换机在将message路由到指定queue时，发现queue没有消费者，则将消息返回个生产者
		//若为false则表示丢弃消息
		//没有消费者监听routingKeyError的队列，immediate=true，ReturnListener会监听到返回消息
		//channel.basicPublish(exchange, routingKeyError, true, null, msg.getBytes());

		//有消费者监听到routingKey的队列，ReturnListener不会监听到返回消息
		channel.basicPublish(exchange, routingKey, true, null, msg.getBytes());

		//添加return listener
		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int replyCode, String replyText, String exchange,
					String routingKey, BasicProperties properties, byte[] body) throws IOException {
				
				System.err.println("---------handle  return----------");
				System.err.println("replyCode: " + replyCode);
				System.err.println("replyText: " + replyText);
				System.err.println("exchange: " + exchange);
				System.err.println("routingKey: " + routingKey);
				System.err.println("properties: " + properties);
				System.err.println("body: " + new String(body));
			}
		});
		
		


		
	}
}
