// package com.coq.rabbitmq.sc01.conusmer;
//
// import com.coq.rabbitmq.sc01.bean.Order01;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.rabbitmq.client.Channel;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.amqp.core.ExchangeTypes;
// import org.springframework.amqp.rabbit.annotation.*;
// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.amqp.support.AmqpHeaders;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.messaging.Message;
// import org.springframework.messaging.MessageHeaders;
// import org.springframework.stereotype.Component;
//
// @Slf4j
// @Component
// public class ReceiverAck {
//
//     @Autowired
//     RabbitTemplate rabbitTemplate;
//
//     /*
//         把RabbitAdmin设置成admin.setIgnoreDeclarationExceptions(true);，这样的好处是即使配置出现了错误也不至于整个应用程序都启动失败的情况。默认情况下，当出现异常时， RabbitAdmin 会立即停止所有声明的处理过程，这就有可能会导致一些问题- 如监听器容器会初始化失败，因另外的队列没有声明，从而web应用启动失败
//
//         # 重试
//         #是否开启消费者重试，消费端代码抛出异常，则重试
//         spring.rabbitmq.listener.simple.retry.enabled=true
//         #最大重试次数
//         spring.rabbitmq.listener.simple.retry.max-attempts=6
//         #重试间隔时间（单位毫秒）
//         spring.rabbitmq.listener.simple.retry.initial-interval=20000
//         #重试次数超过上面的设置之后是否丢弃（false不丢弃时需要写相应代码将该消息加入死信队列）
//         #如果没加死信队列，即使设置为false，重试到达最大次数后也会被丢弃
//         spring.rabbitmq.listener.simple.default-requeue-rejected=false
//
//         # 死信队列
//         队列已经创建，但是没加死信队列；后期加上死信队列，会报错。
//         解决办法一：删除队列重建
//         解决办法二：
//             1、打开rabbitmq控制台 -> admin -> policies -> Add / update a policy
//                 Name: 这个Policy的名称
//                 Pattern: Policy根据正则表达式去匹配Queues/Exchanges名称
//                 Apply to: 这个Policy对Queue还是对Exchange生效，或者两者都适用
//                 Priority: 优先级。
//                 Definition: 添加的args，KV键值对。
//             在控制台设置死信队列，key取消前缀x- 。以下是示例
//             Name	     Pattern	Apply to	Definition	                             Priority
//             dead-policy  queue-ack   queues   dead-letter-exchange:	dead_exchange
//                                               dead-letter-routing-key:	dead_key
//         消费者抛异常的现象：
//             1、执行消费端代码，执行抛出异常代码，但是没有异常堆栈
//             2、进入errorHandler，执行errorHandler代码，但是不抛出异常，也没有异常堆栈
//             3、重复执行 1、2 直到到达最大重试次数
//             4、抛出异常，打印异常堆栈
//             5、进入死信队列消费端代码
//         创建queue时绑定死信列队需要以前缀x-开头，不以x-开头，就要让policy的Pattern能匹配到queue
//         在rabbitmq控制台给queue绑定死信队列，然后添加@Argument注解，key需要取消x-
//      */
//    @RabbitListener(
//         bindings = @QueueBinding(
//             value = @Queue(value = "queue-ack",
//                 durable = "true",
//                 autoDelete = "false"
//                 // arguments = {
//                 //    @Argument(name = "x-dead-letter-exchange", value = DEAD_EXCHANGE),
//                 //    @Argument(name = "x-dead-letter-routing-key", value = DEAD_KEY)
//                 // }
//                 ),
//             exchange = @Exchange(value = "exchange-ack",
//                 durable = "true",
//                 type = ExchangeTypes.DIRECT,
//                 autoDelete = "false",
//                 ignoreDeclarationExceptions = "true"),
//             key = {"key-ack"}),
//         errorHandler = "customListenerErrorHandler"
//    )
//    @RabbitHandler
//    public void onOrderMessage(Message message, Channel channel) throws Exception{
//
//        /**
//         * Message总共有两种
//         * org.springframework.amqp.core.Message 包含较多信息，但是headers默认空的，不好用
//         * org.springframework.messaging.Message 包含 Payload、Headers
//         *   @Payload 如果使用javaBean，生产者、消费者 发送的javaBean必须是同一个类
//         */
//        // byte[] body = message.getBody();
//        // ObjectMapper objectMapper = new ObjectMapper();
//        // Order01 order01 = objectMapper.readValue(body, Order01.class);
//        // System.out.println("消费端："+order01.toString());
//        //
//        // MessageProperties messageProperties = message.getMessageProperties();
//        // Long deliveryTag = messageProperties.getDeliveryTag();
//        // System.out.println("消费端deliveryTag："+deliveryTag);
//
//        /**
//         * org.springframework.messaging.Message 不能修改header
//         */
//        Object payload = message.getPayload();
//        ObjectMapper objectMapper = new ObjectMapper();
//        String payloadStr = payload instanceof String ? (String) payload : objectMapper.writeValueAsString(payload);
//        Order01 order01 = objectMapper.readValue(payloadStr, Order01.class);
//        System.out.println("消费端："+order01.toString());
//
//        MessageHeaders headers = message.getHeaders();
//        Long deliveryTag = (Long)headers.get(AmqpHeaders.DELIVERY_TAG);
//        System.out.println("消费端deliveryTag："+deliveryTag);
//
//        // 可以使用MESSAGE_ID作为消息标识，重回队列后MESSAGE_ID不变
//        String messageId = String.valueOf(headers.get(AmqpHeaders.MESSAGE_ID));
//        System.out.println("消费端messageId："+messageId);
//
//
//        /*
//         channel.basicAck(deliveryTag, multiple);
//         consumer处理成功后，通知broker删除队列中的消息，如果设置multiple=true，表示支持批量确认机制以减少网络流量。
//         例如：有值为5,6,7,8 deliveryTag的投递
//         如果此时channel.basicAck(8, true);则表示前面未确认的5,6,7投递也一起确认处理完毕。
//         如果此时channel.basicAck(8, false);则仅表示deliveryTag=8的消息已经成功处理。
//         */
//        // channel.basicAck(deliveryTag, false);
//
//        /*
//         consumer处理失败后，例如：有值为5,6,7,8 deliveryTag的投递。
//         如果channel.basicNack(8, true, true);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息重新放回队列中。
//         如果channel.basicNack(8, true, false);表示deliveryTag=8之前未确认的消息都处理失败且将这些消息直接丢弃。
//         如果channel.basicNack(8, false, true);表示deliveryTag=8的消息处理失败且将该消息重新放回队列。
//         如果channel.basicNack(8, false, false);表示deliveryTag=8的消息处理失败且将该消息直接丢弃。
//         */
//        // channel.basicNack(deliveryTag, false, true);
//        /*
//        spring.rabbitmq.listener.simple.acknowledge-mode=manual
//        手动ack确认，抛出异常
//            1、不重试
//            2、消息不会从队列移除
//            3、消费端重启，再次消费消息
//
//        spring.rabbitmq.listener.simple.acknowledge-mode=auto  // auto是默认值
//        自动ack确认，，抛出异常
//            1、一直重试
//
//         */
//
//        // if (order01.getId() > 100){
//        //     // 签收
//        //     channel.basicAck(deliveryTag, false);
//        // }else {
//        //     // acknowledge-mode=manual
//        //     // 重回队列，会一直重试，retry.max-attempts无效
//        //     channel.basicNack(deliveryTag, false, true);
//
//            // acknowledge-mode=auto 抛异常导致的重试，重试期间阻塞消费端消费其他消息
//            throw new RuntimeException("抛异常");
//        // }
//
//    }
//
// }
