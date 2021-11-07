package com.coq.rabbitmq.sb.conusmer;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MqttReceiver {

    /**
     * 接受MQTT
     * MQTT的topic就是Rabbitmq的routingKey
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
                    durable = "${spring.rabbitmq.listener.order.queue.durable}",
                    arguments = {
                            @Argument(name = "x-dead-letter-exchange", value = "${custom.rabbit.exchange.direct.dead.exchange}"),
                            @Argument(name = "x-dead-letter-routing-key", value = "${custom.rabbit.routingkey.direct.dead}")
                    }),

            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),

            key = {"${spring.rabbitmq.listener.order.key}"}
    ))
    @RabbitHandler
    public void onOrderMessage(@Payload byte[] msgByte,
                               Channel channel,
                               @Headers Map<Object, Object> headers) throws Exception {

        System.out.println("消费端：" + new String(msgByte));
        //Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        System.out.println("headers  " + headers.toString());

        // 签收信息
        //channel.basicAck(deliveryTag, false);


    }

}
