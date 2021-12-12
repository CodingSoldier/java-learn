package com.coq.rabbitmq.sc01.conusmer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReceiverSpecific {

    /*
    使用控制台像特定的 consumer 发送消息，rabbitmq不提供此功能，但是可以一定的技巧实现
	1、消费端 @RabbitListener 的 @Queue 注解添加两个属性
	durable = "false",   // 不持久化
	autoDelete = "true"   // 队列不再使用时自动删除，关闭程序时会自动删除
	2、确保此队列的消费者只有一个

控制台发送消息：
	1、properties 添加 content_type=text/plain
	2、Payload填写：{"id": 12345, "name": "中文名"}
	3、点击发送
     */
   @RabbitListener(bindings = @QueueBinding(
           value = @Queue(value = "specific-listener-queue-cpq-test",
                   // exclusive = "true",
                   durable = "false",
                   autoDelete = "true"
                   // durable = "true"
                   ),
           exchange = @Exchange(value = "specific-listener-exchange",
                   // durable = "true",
                   durable = "false",
                   type = "topic",
                   ignoreDeclarationExceptions = "true"),
           key = {"specific-listener-key"}
   ))
   @RabbitHandler
   public void onOrderMessage(@Payload String str,
                              Channel channel,
                              @Headers Map<String, Object> headers) throws Exception{

       System.out.println("消费端："+str.toString());
       Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
       System.out.println("deliveryTag  "+deliveryTag);

       // 签收信息
       channel.basicAck(deliveryTag, false);

   }

}
