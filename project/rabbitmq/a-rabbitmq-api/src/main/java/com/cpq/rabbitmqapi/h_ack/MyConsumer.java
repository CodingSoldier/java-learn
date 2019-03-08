package com.cpq.rabbitmqapi.h_ack;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

public class MyConsumer extends DefaultConsumer {


	private Channel channel ;
	
	public MyConsumer(Channel channel) {
		super(channel);
		this.channel = channel;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
		System.err.println("-----------consume message----------");
		System.err.println("body: " + new String(body));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//如果是0则Nack不签收，并把消息返回给生产者（重回队列）
		if((Integer)properties.getHeaders().get("num") == 0) {
			channel.basicNack(envelope.getDeliveryTag(), false, true);
		} else {
			//签收
			channel.basicAck(envelope.getDeliveryTag(), false);
		}
		
	}


}
