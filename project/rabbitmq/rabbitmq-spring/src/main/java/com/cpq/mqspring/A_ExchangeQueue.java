package com.cpq.mqspring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class A_ExchangeQueue {

	@Test
	public void contextLoads() {
	}
	
	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Test
	public void testAdmin() throws Exception {

		// 创建交换机
		rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
		rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
		rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));

		//队列
		rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
		rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
		rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));

		//绑定
		rabbitAdmin.declareBinding(new Binding("test.direct.queue",
				Binding.DestinationType.QUEUE,
				"test.direct", "direct", new HashMap<>()));

		//链式绑定
		rabbitAdmin.declareBinding(
				BindingBuilder
						.bind(new Queue("test.topic.queue", false))		//直接创建队列
						.to(new TopicExchange("test.topic", false, false))	//直接创建交换机 建立关联关系
						.with("user.#"));	//指定路由Key
		//fanout是直连，不需要routingKey
		rabbitAdmin.declareBinding(
				BindingBuilder
						.bind(new Queue("test.fanout.queue",false))
						.to(new FanoutExchange("test.fanout", false, false)));

		//清空队列数据
		rabbitAdmin.purgeQueue("test.topic.queue", false);

	}

}
