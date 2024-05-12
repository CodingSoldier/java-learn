package com.coq.rabbitmq.sp01.producer;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * <p>
 * ip网段对应地址 前端控制器
 * </p>
 *
 * @author chenpiqian
 * @since 2019-04-24
 */
@RequestMapping("/test")
@Slf4j
@RestController
public class TestController {

    @Autowired
    RabbitAdmin rabbitAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    ConnectionFactory connectionFactory;

    String exchangeName = "test.topic";
    String routingKey = "test.device.test";

    @GetMapping("/exchange")
    public String exchange() throws Exception{
        TopicExchange topicExchange = new TopicExchange(exchangeName, true, false);
        rabbitAdmin.declareExchange(topicExchange);
        return "OK";
    }

    @GetMapping("/send")
    public String send() throws Exception{
        rabbitTemplate.convertAndSend(exchangeName, routingKey, "dafdasdfa3fsdfsfsdf", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                log.info("##############msg={}", message);
                return message;
            }
        });
        return "OK";
    }


    // public CachingConnectionFactory connectionFactory() throws Exception{
    //     CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    //
    //     connectionFactory.setUsername("guest");
    //     connectionFactory.setPassword("guest");
    //     connectionFactory.setVirtualHost("/");
    //
    //     connectionFactory.setPublisherConfirms(true);
    //
    //     //该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
    //     connectionFactory.setAddresses("127.0.0.1:5672");
    //     return connectionFactory;
    // }

    @GetMapping("/listener")
    public String listener() throws Exception{
        // queue
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        String testDeviceQueueName = "test_device_queue";
        Queue testDeviceQueue = new Queue(testDeviceQueueName, true, false, false, arguments);
        rabbitAdmin.declareQueue(testDeviceQueue);

        // binding
        TopicExchange topicExchange = new TopicExchange(exchangeName);
        Binding binding = BindingBuilder.bind(testDeviceQueue).to(topicExchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);

        Channel channel = connectionFactory.createConnection().createChannel(false);
        channel.basicConsume(testDeviceQueueName, new MyConsumer(channel));
        return "OK";
    }

    @GetMapping("/listener2")
    public String listener2() throws Exception{
        // queue
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        String testDeviceQueueName = "test_device_queue2";
        Queue testDeviceQueue = new Queue(testDeviceQueueName, true, false, false, arguments);
        rabbitAdmin.declareQueue(testDeviceQueue);

        // binding
        TopicExchange topicExchange = new TopicExchange(exchangeName);
        Binding binding = BindingBuilder.bind(testDeviceQueue).to(topicExchange).with("test.#.#");
        rabbitAdmin.declareBinding(binding);

        Channel channel = connectionFactory.createConnection().createChannel(false);
        channel.basicConsume(testDeviceQueueName, new MyConsumer2(channel));
        return "OK";
    }
}
