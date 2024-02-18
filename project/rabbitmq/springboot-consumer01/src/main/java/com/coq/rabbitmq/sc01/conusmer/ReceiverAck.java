 package com.coq.rabbitmq.sc01.conusmer;

 import com.coq.rabbitmq.sc01.bean.Order01;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.rabbitmq.client.Channel;
 import java.math.BigDecimal;
 import java.util.Date;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.amqp.core.ExchangeTypes;
 import org.springframework.amqp.rabbit.annotation.*;
 import org.springframework.amqp.rabbit.core.RabbitTemplate;
 import org.springframework.amqp.support.AmqpHeaders;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.messaging.Message;
 import org.springframework.messaging.MessageHeaders;
 import org.springframework.stereotype.Component;

 @Component
 public class ReceiverAck {

     @Autowired
     RabbitTemplate rabbitTemplate;

   /**
    * 监听福讯支付通知
    */
   @RabbitListener(bindings = @QueueBinding(
       exchange = @Exchange(value = "02q44LTW8O9Xb8uK_thing",type = "topic"),
       key = "2R3qQJs42It.event.#",
       value = @Queue(value = "02q44LTW8O9Xb8uK_ibms_b11")
   )
   )
   public void basicDataListener(Message<String> message, Channel channel) throws Exception {
     MessageHeaders headers = message.getHeaders();
     Object deliveryTagObject = headers.get(AmqpHeaders.DELIVERY_TAG);
     String payload = message.getPayload();
     System.out.println("收到福讯账单mq消息");
     System.out.println(deliveryTagObject);
     System.out.println(payload);


     //手动ack
     // 签收
     //channel.basicAck(deliveryTag, false);
   }

 }
