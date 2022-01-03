package com.coq.rabbitmq.sp01.producer;

import com.coq.rabbitmq.sp01.bean.Order01;
import com.coq.rabbitmq.sp01.config.MyRabbitmqConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SenderAck {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	// @Qualifier("callbackRabbitTemplate")
	// @Autowired
	// private RabbitTemplate callbackRabbitTemplate;

	public void sendOrder(Order01 order) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		String orderStr = objectMapper.writeValueAsString(order);

		String id = UUID.randomUUID().toString();
		// CorrelationData 对象内部只有一个 id 属性，用来表示当前消息唯一性。
		CorrelationData correlationData = new CorrelationData(id);
        System.out.println("correlationData   "+id);

		System.out.println("hashCode: " + rabbitTemplate.hashCode());
		// System.out.println("hashCode: " + callbackRabbitTemplate.hashCode());

		/**
		 * 不设置setConfirmCallback、setReturnCallback，信息发送错误，也没有任何报错
		 * correlationData 在 ConfirmCallback 接口中作为参数返回
		 */
		// rabbitTemplate.convertAndSend("exchange-ack", "key-ack", orderStr, correlationData);

		/**
		 * 加入MessagePostProcessor，在对MessageProperties增强
		 */
		rabbitTemplate.convertAndSend("exchange-ack", "key-ack", orderStr, MyRabbitmqConfig.createMessagePostProcess(), correlationData);

	}

	// public void sendOrderCallback(Order01 order) throws Exception{
	// 	ObjectMapper objectMapper = new ObjectMapper();
	// 	String orderStr = objectMapper.writeValueAsString(order);
	//
	// 	String id = UUID.randomUUID().toString();
	// 	// CorrelationData 对象内部只有一个 id 属性，用来表示当前消息唯一性。
	// 	CorrelationData correlationData = new CorrelationData(id);
    //     System.out.println("correlationData   "+id);
	//
	// 	System.out.println("hashCode: " + callbackRabbitTemplate.hashCode());
	//
	// 	callbackRabbitTemplate.convertAndSend("exchange-ack", "key-ack", orderStr, correlationData);
	// }

}











