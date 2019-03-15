package com.coq.rabbitmq.springboot.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	//回调函数: confirm确认，消息能路由到queue，走这里
	final RabbitTemplate.ConfirmCallback confirmCallback= new RabbitTemplate.ConfirmCallback(){
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause){
			System.out.println("confirm");
			System.out.println("correlationData  "+correlationData);
			System.out.println("ack " +ack);
			if (!ack){
				System.out.println("1、没找到交换机。2、有交换机但是交换机没有routingKey。异常处理");
			}
		}
	};

	//回调函数: return返回
	//有交换机且有routingKey，但是routingKey路由不到队列
	final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
		@Override
		public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
			System.err.println("return exchange: " + exchange + ", routingKey: "
					+ routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
		}
	};

	//运行testSender1()测试
	public void send(Object message, Map<String, Object> properties) throws Exception{
		MessageHeaders mhs = new MessageHeaders(properties);
		Message msg = MessageBuilder.createMessage(message,mhs);
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		//id必须是全局唯一
		CorrelationData correlationData = new CorrelationData("1234567890");
		//要先在rabbitmq中新建exchange-1，并通过routingKey路由到一个队列
		rabbitTemplate.convertAndSend("exchange-1", "springboot.def", msg, correlationData);
	}

}











