package com.coq.rabbitmq.springboot.conusmer;

import com.coq.rabbitmq.springboot.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class RabbitReceiver {

    /*
    使用注解@RabbitListener、@RabbitHandler即可实现消费者监听。

    并且当rabbitmq中没有exchange、queue、以及exchange和queue的绑定关系时，启动消费者后，会在rabbitmq上建立这3者。不需要我们在服务器上先建立

    把RabbitAdmin设置成admin.setIgnoreDeclarationExceptions(true);，这样的好处是即使配置出现了错误也不至于整个应用程序都启动失败的情况。默认情况下，当出现异常时， RabbitAdmin 会立即停止所有声明的处理过程，这就有可能会导致一些问题- 如监听器容器会初始化失败，因另外的队列没有声明，从而web应用启动失败
    */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "queue-1", durable = "true"),
            exchange = @Exchange(value = "exchange-1", durable = "true", type = "topic", ignoreDeclarationExceptions = "true"),
            key = "springboot.*"
    ))
    @RabbitHandler
    public void onMessage(Message message, Channel channel) throws Exception{
        System.out.println("消费端Payload: "+message.getPayload());
        Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        //手动ack
        channel.basicAck(deliveryTag, false);
    }


    /**
     1、使用${}获取配置文件中的信息
     2、@Payload、@Headers 其实是将Message这个参数拆开
     3、运行rabbitmq-springboot-producer中的ApplicationTests#testSender2()
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
                    durable = "${spring.rabbitmq.listener.order.queue.durable}"),

            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
                    type = "${spring.rabbitmq.listener.order.exchange.type}",
                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),

            key = "${spring.rabbitmq.listener.order.key}"
    ))
    @RabbitHandler
    public void onOrderMessage(@Payload Order order,
                               Channel channel,
                               @Headers Map<String, Object> headers) throws Exception{
        System.out.println("消费端："+order.toString());
        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
        //手动ack
        channel.basicAck(deliveryTag, false);
    }



}
