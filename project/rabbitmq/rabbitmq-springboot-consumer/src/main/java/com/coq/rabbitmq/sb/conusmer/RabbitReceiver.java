//package com.coq.rabbitmq.sb.conusmer;
//
//import com.coq.rabbitmq.sb.Const;
//import com.example.rabbitmqbean.MyOrder;
//import com.rabbitmq.client.Channel;
//import org.springframework.amqp.rabbit.annotation.*;
//import org.springframework.amqp.support.AmqpHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageHeaders;
//import org.springframework.messaging.handler.annotation.Headers;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//public class RabbitReceiver {
//
//    /*
//    使用注解@RabbitListener、@RabbitHandler即可实现消费者监听。
//
//    并且当rabbitmq中没有exchange、queue、以及exchange和queue的绑定关系时，启动消费者后，会在rabbitmq上建立这3者。不需要我们在服务器上先建立
//
//    把RabbitAdmin设置成admin.setIgnoreDeclarationExceptions(true);，这样的好处是即使配置出现了错误也不至于整个应用程序都启动失败的情况。默认情况下，当出现异常时， RabbitAdmin 会立即停止所有声明的处理过程，这就有可能会导致一些问题- 如监听器容器会初始化失败，因另外的队列没有声明，从而web应用启动失败
//    */
//    //@RabbitListener(bindings = @QueueBinding(
//    //        value = @Queue(value = "queue-1", durable = "true"),
//    //        exchange = @Exchange(value = "exchange-1", durable = "true", type = "topic", ignoreDeclarationExceptions = "true"),
//    //        key = "springboot.*"
//    //))
//    //@RabbitHandler
//    //public void onMessage(Message message, Channel channel) throws Exception{
//    //    System.out.println("消费端Payload: "+message.getPayload());
//    //    Long deliveryTag = (Long)message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
//    //    //手动ack
//    //    //channel.basicAck(deliveryTag, false);
//    //}
//
//
//    /**
//     1、使用${}获取配置文件中的信息
//     2、@Payload、@Headers 其实是将Message这个参数拆开
//     3、运行rabbitmq-springboot-producer中的ApplicationTests#testSender2()
//
//     arguments：x-dead-letter-exchange、x-dead-letter-routing-key 设置死信队列
//     改变arguments参数重新部署时需要先删除队列
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = "${spring.rabbitmq.listener.order.queue.name}",
//                    durable = "${spring.rabbitmq.listener.order.queue.durable}",
//                    arguments = {
//                            @Argument(name = "x-dead-letter-exchange", value = "${custom.rabbit.exchange.direct.dead.exchange}"),
//                            @Argument(name = "x-dead-letter-routing-key", value = "${custom.rabbit.routingkey.direct.dead}")
//                    }),
//
//            exchange = @Exchange(value = "${spring.rabbitmq.listener.order.exchange.name}",
//                    durable = "${spring.rabbitmq.listener.order.exchange.durable}",
//                    type = "${spring.rabbitmq.listener.order.exchange.type}",
//                    ignoreDeclarationExceptions = "${spring.rabbitmq.listener.order.exchange.ignoreDeclarationExceptions}"),
//
//            key = {"${spring.rabbitmq.listener.order.key}"}
//    ))
//    @RabbitHandler
//    public void onOrderMessage(@Payload MyOrder order,
//                               Channel channel,
//                               @Headers Map<String, Object> headers) throws Exception{
//
//        System.out.println("消费端："+order.toString());
//        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
//        System.out.println("deliveryTag  "+deliveryTag);
//
//        // 签收信息
//        channel.basicAck(deliveryTag, false);
//
//        if ((Integer.parseInt(order.getId())%2) == 0){
//            System.out.println("channel.basicAck(deliveryTag, false);....");
//            //手动ack
//            //channel.basicAck(deliveryTag, false);
//        }else {
//
//            // 拒绝ack，不重回队列，消息路由到死信队列
//            //System.out.println("channel.basicNack(deliveryTag, false, false)");
//            //channel.basicNack(deliveryTag, false, false);
//
//            // 拒绝ack，重回队列
//            //System.out.println("channel.basicNack(deliveryTag, false, true);");
//            //channel.basicNack(deliveryTag, false, true);
//
//            //抛出异常用于测试，重试机制
//            //throw new RuntimeException();
//        }
//
//    }
//
//
//    /**
//     * 死信队列消费者，由于消息体内容千变万化，不能使用@Payload Object接受，@Payload只接受javabean
//     */
//    @RabbitListener(bindings = @QueueBinding(
//            value = @Queue(value = Const.DIRECT_DEAD_QUEUE,
//                    durable = "${custom.rabbit.durable.true}"),
//
//            exchange = @Exchange(value = "${custom.rabbit.exchange.direct.dead.exchange}",
//                    durable = "${custom.rabbit.durable.true}",
//                    type = Const.DIRECT,
//                    ignoreDeclarationExceptions = "${custom.rabbit.ignoredeclarationexceptions.true}"),
//
//            key = "${custom.rabbit.routingkey.direct.dead}"
//    ))
//    @RabbitHandler
//    public void onDeadQueueMessage(Message message, Channel channel) throws Exception{
//        Object dataObj = message.getPayload();
//        System.out.println("dataObj   "+dataObj.toString());
//
//
//        MessageHeaders headers = message.getHeaders();
//
//        Object xDeath = headers.get("x-death");
//        if (xDeath instanceof ArrayList){
//            ArrayList arrayList = (ArrayList)xDeath;
//            System.out.println(arrayList.toString());
//            for (Object elem:arrayList){
//                if (elem instanceof Map){
//                    String fromExchange = ((Map) elem).get("exchange").toString();
//                    String fromRoutingKeys = ((Map) elem).get("routing-keys").toString();
//                    String fromQueue = ((Map) elem).get("queue").toString();
//                    Object intoDeadExchangeTime = ((Map) elem).get("time");
//                }
//            }
//            arrayList.add(new HashMap<String, Integer>(){{put("aaa", 11);}});
//        }
//        headers.put("x-death", xDeath);
//
//        //channel.basicAck((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false);
//        channel.basicNack((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false, true);
//    }
//
//}
