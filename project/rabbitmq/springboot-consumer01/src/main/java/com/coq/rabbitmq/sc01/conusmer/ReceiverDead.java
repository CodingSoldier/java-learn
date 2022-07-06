// package com.coq.rabbitmq.sc01.conusmer;
//
// import com.rabbitmq.client.Channel;
// import org.springframework.amqp.core.ExchangeTypes;
// import org.springframework.amqp.rabbit.annotation.*;
// import org.springframework.amqp.support.AmqpHeaders;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.MessageHeaders;
// import org.springframework.stereotype.Component;
//
// import static com.coq.rabbitmq.sc01.config.RabbitmqConstant.*;
//
// @Component
// public class ReceiverDead {
//
//     /**
//     * 死信队列消费者，由于消息体内容千变万化，不能使用@Payload Object接受，@Payload只接受javabean
//     */
//    @RabbitListener(
//        bindings = @QueueBinding(
//            value = @Queue(value = DEAD_QUEUE,
//                durable = "true",
//                autoDelete = "false"),
//            exchange = @Exchange(value = DEAD_EXCHANGE,
//                durable = "true",
//                type = ExchangeTypes.DIRECT,
//                ignoreDeclarationExceptions = "true"),
//            key = {DEAD_KEY})
//    )
//    @RabbitHandler
//    public void onDeadQueueMessage(Message message, Channel channel) throws Exception{
//        try {
//            Object dataObj = message.getPayload();
//            System.out.println("死信队列，dataObj："+dataObj.toString());
//
//            MessageHeaders headers = message.getHeaders();
//            Object deliveryTag = headers.get(AmqpHeaders.DELIVERY_TAG);
//            System.out.println("死信队列，deliveryTag: " + deliveryTag.toString());
//            channel.basicAck((Long)deliveryTag, false);
//
//            // Object xDeath = headers.get("x-death");
//            // if (xDeath instanceof ArrayList){
//            //     ArrayList arrayList = (ArrayList)xDeath;
//            //     System.out.println(arrayList.toString());
//            //     for (Object elem:arrayList){
//            //         if (elem instanceof Map){
//            //             String fromExchange = ((Map) elem).get("exchange").toString();
//            //             String fromRoutingKeys = ((Map) elem).get("routing-keys").toString();
//            //             String fromQueue = ((Map) elem).get("queue").toString();
//            //             Object intoDeadExchangeTime = ((Map) elem).get("time");
//            //         }
//            //     }
//            //     arrayList.add(new HashMap<String, Integer>(){{put("aaa", 11);}});
//            // }
//            // headers.put("x-death", xDeath);
//
//            //channel.basicAck((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false);
//            // channel.basicNack((Long)headers.get(AmqpHeaders.DELIVERY_TAG), false, true);
//        } catch (Exception e){
//            // 不能出错
//            System.out.println("死信队列异常");
//            e.printStackTrace();
//        }
//    }
// }
