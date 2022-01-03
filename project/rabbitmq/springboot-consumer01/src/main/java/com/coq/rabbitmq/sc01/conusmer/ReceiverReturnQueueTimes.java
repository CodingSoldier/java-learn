package com.coq.rabbitmq.sc01.conusmer;

import com.coq.rabbitmq.sc01.bean.Order01;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.coq.rabbitmq.sc01.config.RabbitmqConstant.DEAD_EXCHANGE;
import static com.coq.rabbitmq.sc01.config.RabbitmqConstant.DEAD_KEY;

@Slf4j
@Component
public class ReceiverReturnQueueTimes {

    @Autowired
    RabbitTemplate rabbitTemplate;

   @RabbitListener(
        bindings = @QueueBinding(
            value = @Queue(value = "queue-return-queue-times",
                durable = "true",
                autoDelete = "false",
                arguments = {
                   @Argument(name = "x-dead-letter-exchange", value = DEAD_EXCHANGE),
                   @Argument(name = "x-dead-letter-routing-key", value = DEAD_KEY)
                }
                ),
            exchange = @Exchange(value = "exchange-return-queue-times",
                durable = "true",
                type = ExchangeTypes.DIRECT,
                autoDelete = "false",
                ignoreDeclarationExceptions = "true"),
            key = {"key-return-queue-times"}),
        errorHandler = "customListenerErrorHandler"
   )
   @RabbitHandler
   public void onOrderMessage(Message message, Channel channel) throws Exception{

       byte[] body = message.getBody();
       ObjectMapper objectMapper = new ObjectMapper();
       Order01 order01 = objectMapper.readValue(body, Order01.class);
       System.out.println("消费端："+order01.toString());

       MessageProperties messageProperties = message.getMessageProperties();

       Long deliveryTag = messageProperties.getDeliveryTag();
       System.out.println("消费端deliveryTag："+deliveryTag);

       String messageId = messageProperties.getMessageId();
       System.out.println("消费端messageId："+messageId);

       // 发送端需在MessagePostProcessor添加return-queues-times
       Map<String, Object> headers = messageProperties.getHeaders();
       Integer returnQueuesTimes = Integer.parseInt(headers.get("return-queues-times").toString());
       System.out.println("消费端returnQueuesTimes："+returnQueuesTimes);


       if (order01.getId() > 100){
           // 签收
           channel.basicAck(deliveryTag, false);
       }else {
           if (returnQueuesTimes < 10){
               channel.basicNack(deliveryTag, false, true);

               /**
                * spring.rabbitmq.listener.simple.acknowledge-mode=manual
                *
                * org.springframework.messaging.Message 修改header会报错
                * org.springframework.amqp.core.Message 设置headers，重回队列后无效
                *
                * header 不能承载 重试次数 ？
                */
               messageProperties.setHeader("return-queues-times", ++returnQueuesTimes);
           } else {
               rabbitTemplate.convertAndSend(DEAD_EXCHANGE, DEAD_KEY, objectMapper.writeValueAsString(order01));
           }

           // acknowledge-mode=auto 抛异常导致的重试，重试期间阻塞消费端消费其他消息
           // throw new RuntimeException("抛异常");
       }

   }

}
