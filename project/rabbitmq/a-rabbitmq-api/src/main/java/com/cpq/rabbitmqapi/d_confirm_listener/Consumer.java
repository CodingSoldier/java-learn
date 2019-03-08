package com.cpq.rabbitmqapi.d_confirm_listener;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer {

	
	public static void main(String[] args) throws Exception {

		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost("192.168.40.129");
		connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String exchangeName = "test_confirm_exchange";
		String routingKey = "d_confirm_listener.#";
		String queueName = "test_confirm_queue";
		
		//4 声明交换机和队列 然后进行绑定设置, 最后制定路由Key
		//exchangeDeclare第三个参数是否持久化
		channel.exchangeDeclare(exchangeName, "topic", true);
		//queueDeclare(queueName,持久化, exclusive独占模式, 自动删除, arguments参数)
		channel.queueDeclare(queueName, true, false, false, null);
		channel.queueBind(queueName, exchangeName, routingKey);
		
		//5 创建消费者 
		QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
		//basicConsume(queueName, autoAck, Callback)
		channel.basicConsume(queueName, true, queueingConsumer);
		
		while(true){
			Delivery delivery = queueingConsumer.nextDelivery();
			String msg = new String(delivery.getBody());
			
			System.err.println("消费端: " + msg);
		}
		
		
	}
}
