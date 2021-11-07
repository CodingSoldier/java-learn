package com.coq.rabbitmq.springboot.producer;

import com.example.rabbitmqbean.MyOrder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitSender {

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

    //
	////运行testSender1()测试
	//public void send(Object message, Map<String, Object> properties) throws Exception{
	//	MessageHeaders mhs = new MessageHeaders(properties);
	//	Message msg = MessageBuilder.createMessage(message,mhs);
	//	rabbitTemplate.setConfirmCallback(confirmCallback);
	//	rabbitTemplate.setReturnCallback(returnCallback);
	//	//id必须是全局唯一
	//	CorrelationData correlationData = new CorrelationData(Math.random()*100000D+"");
	//	//要先在rabbitmq中新建exchange-1，并通过routingKey路由到一个队列
	//	rabbitTemplate.convertAndSend("exchange-1", "springboot.def", msg, correlationData);
	//}


	/**
	 * 运行ApplicationTests#testSender2()
	 * commonproducer-springboot-consumer项目的RabbitReceiver#onOrderMessage()收到消息
	 */
	public void sendOrder(MyOrder order) throws Exception{
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		String num = ((int)Math.ceil(Math.random()*100000))+"";
		CorrelationData correlationData = new CorrelationData(num);
        System.out.println("correlationData   "+num);

        // 如果routingKey与实际理论不符合，可能是交换机已经创建过，修改类型是无法生效的
		rabbitTemplate.convertAndSend("test-exchange-2", "routing-key-2", order, correlationData);

		//rabbitTemplate.convertAndSend("routing-key-2", order, correlationData);

		//rabbitTemplate.convertAndSend("test-exchange-2", "test-exchange-2.abc", order, correlationData);
	}

	public void sendDead(MyOrder order) throws Exception{
		rabbitTemplate.setConfirmCallback(confirmCallback);
		rabbitTemplate.setReturnCallback(returnCallback);
		String num = ((int)Math.ceil(Math.random()*100000))+"";
		CorrelationData correlationData = new CorrelationData(num);
        System.out.println("correlationData   "+num);
		rabbitTemplate.convertAndSend("direct-dead-exchange", "direct-dead", order, correlationData);
	}

}











