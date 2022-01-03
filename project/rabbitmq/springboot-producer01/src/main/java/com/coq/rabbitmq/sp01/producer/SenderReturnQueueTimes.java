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
public class SenderReturnQueueTimes {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public void sendOrder(Order01 order) throws Exception{
		ObjectMapper objectMapper = new ObjectMapper();
		String orderStr = objectMapper.writeValueAsString(order);

		String id = UUID.randomUUID().toString();
		CorrelationData correlationData = new CorrelationData(id);
        System.out.println("correlationData   "+id);

        /**
         * 加入MessagePostProcessor，在对MessageProperties增强
		 */
		rabbitTemplate.convertAndSend("exchange-return-queue-times", "key-return-queue-times", orderStr, MyRabbitmqConfig.createMessagePostProcess(), correlationData);
	}

}











