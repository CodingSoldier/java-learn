package com.coq.rabbitmq.sc01.conusmer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ReceiverReTry {

   @RabbitListener(bindings = @QueueBinding(
           value = @Queue(value = "queue-retry",
                   durable = "true"
                   ),
           exchange = @Exchange(value = "exchange-retry",
                   durable = "true",
                   type = ExchangeTypes.DIRECT,
                   ignoreDeclarationExceptions = "true"),
           key = {"retry-key"}
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
