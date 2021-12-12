package com.coq.rabbitmq.sp01.producer;

import com.coq.rabbitmq.sp01.bean.Order01;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SenderRetry {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * rabbitmq 整个消息投递的路径为：
	 * producer->rabbitmq broker cluster->exchange->queue->consumer
	 *
	 * message 从 producer 到 rabbitmq broker cluster 则会返回一个 confirmCallback
	 * message 从 exchange->queue 投递失败则会返回一个 returnCallback 。
	 */
	final RabbitTemplate.ConfirmCallback confirmCallback= new RabbitTemplate.ConfirmCallback(){
		@Override
		public void confirm(CorrelationData correlationData, boolean ack, String cause){
			System.out.println("ConfirmCallback##correlationData "+correlationData.toString());
			System.out.println("ConfirmCallback##ack " +ack);
			System.out.println("ConfirmCallback##cause " +cause);
			if (!ack){
				System.out.println("1、没找到交换机。2、有交换机但是交换机没有routingKey。异常处理");
			}
		}
	};

	// 回调函数: return返回
	// 有交换机且有routingKey，但是routingKey路由不到队列
	final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
		@Override
		public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
			System.err.println("return exchange: " + exchange + ", routingKey: "
					+ routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
		}
	};

	/**
	 * 运行ApplicationTests#testSender2()
	 * commonproducer-springboot-consumer项目的RabbitReceiver#onOrderMessage()收到消息
	 */
	public void sendOrder(Order01 order) throws Exception{
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		String num = ((int)Math.ceil(Math.random()*100000))+"";
		CorrelationData correlationData = new CorrelationData(num);
        System.out.println("correlationData   "+num);
		ObjectMapper objectMapper = new ObjectMapper();
		String orderStr = objectMapper.writeValueAsString(order);
		rabbitTemplate.convertAndSend("exchange-retry", "retry-key", orderStr, correlationData);

	}

	public void sendDead(Order01 order) throws Exception{
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		String num = ((int)Math.ceil(Math.random()*100000))+"";
		CorrelationData correlationData = new CorrelationData(num);
        System.out.println("correlationData   "+num);
		ObjectMapper objectMapper = new ObjectMapper();
		String orderStr = objectMapper.writeValueAsString(order);
		rabbitTemplate.convertAndSend("direct-dead-exchange", "direct-dead", orderStr, correlationData);
	}

}











