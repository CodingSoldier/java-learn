package com.cpq.rabbitmqapi.b_exchange.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;

public class Consumer4TopicExchange {

	public static void main(String[] args) throws Exception {
		
		
        ConnectionFactory connectionFactory = new ConnectionFactory() ;

        connectionFactory.setHost("192.168.40.129");
        connectionFactory.setPort(5672);
		connectionFactory.setVirtualHost("/");
		
        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);
        Connection connection = connectionFactory.newConnection();
        
        Channel channel = connection.createChannel();  

		String exchangeName = "test_topic_exchange";
		String exchangeType = "topic";  //通配符模式
		String queueName = "test_topic_queue";
		//String routingKey = "user.#"; //匹配0个或多个词，生产者交换机的routingKey可为：user、user.update、user.delete.abc
		String routingKey = "user.*"; //匹配一个词，生产者交换机的routingKey可为：user.update

		channel.exchangeDeclare(exchangeName, exchangeType, true, false, false, null);

		channel.queueDeclare(queueName, false, false, false, null);

		channel.queueBind(queueName, exchangeName, routingKey);
		

        QueueingConsumer consumer = new QueueingConsumer(channel);

        channel.basicConsume(queueName, true, consumer);  

        while(true){  

            Delivery delivery = consumer.nextDelivery();  
            String msg = new String(delivery.getBody());    
            System.out.println("收到消息：" + msg);  
        } 
	}
}
