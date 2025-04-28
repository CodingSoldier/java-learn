package com.coq.rabbitmq.sp01.notuserspringcomsumer;


import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
public class CreateConsumerController {

    public Map<String, CachingConnectionFactory> connMap = new ConcurrentHashMap<>();
    public Map<String, RabbitAdmin> adminMap = new ConcurrentHashMap<>();

    // String exchangeName = "test.topic";
    // String routingKey = "test.device.test";

    private CachingConnectionFactory connectionFactory(String username, String password, String virtualHost, String address) throws Exception{
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);

        connectionFactory.setPublisherConfirms(true);

        //该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
        connectionFactory.setAddresses(address);
        return connectionFactory;
    }

    @GetMapping("/init/connection-admin")
    public String initConnectionAdmin(String username, String password, String virtualHost, String address) throws Exception {
        String key = address + virtualHost;
        synchronized (this) {
            if (!connMap.containsKey(key)) {
                CachingConnectionFactory cachingConnectionFactory = connectionFactory(username, password, virtualHost, address);
                connMap.put(key, cachingConnectionFactory);
            }
            if (!adminMap.containsKey(key)) {
                CachingConnectionFactory cachingConnectionFactory = connMap.get(key);
                RabbitAdmin rabbitAdmin = new RabbitAdmin(cachingConnectionFactory);
                adminMap.put(key, rabbitAdmin);
            }
        }
        return "OK";
    }


    @GetMapping("/exchange")
    public String exchange(String key, String exchangeName) throws Exception{
        TopicExchange topicExchange = new TopicExchange(exchangeName, true, false);
        RabbitAdmin rabbitAdmin = adminMap.get(key);
        rabbitAdmin.declareExchange(topicExchange);
        return "OK";
    }

    @GetMapping("/listener")
    public String listener(String key, String exchangeName, String routingKey, String testDeviceQueueName) throws Exception{
        // queue
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        Queue testDeviceQueue = new Queue(testDeviceQueueName, true, false, false, arguments);
        RabbitAdmin rabbitAdmin = adminMap.get(key);
        rabbitAdmin.declareQueue(testDeviceQueue);

        // binding
        TopicExchange topicExchange = new TopicExchange(exchangeName);
        Binding binding = BindingBuilder.bind(testDeviceQueue).to(topicExchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);
        CachingConnectionFactory connectionFactory = connMap.get(key);
        synchronized (this) {
            Channel channel = connectionFactory.createConnection().createChannel(false);
            long count = channel.consumerCount(testDeviceQueueName);
            System.out.println("######listener##consumerCount="+count);
            if (count == 0) {
                channel.basicConsume(testDeviceQueueName, new Consumer01(channel));
            }
        }
        return "OK";
    }

    @GetMapping("/listener2")
    public String listener2(String key, String exchangeName, String routingKey, String testDeviceQueueName) throws Exception{
        // queue
        HashMap<String, Object> arguments = new HashMap<>();
        arguments.put("x-message-ttl", 10000);
        Queue testDeviceQueue = new Queue(testDeviceQueueName, true, false, false, arguments);
        RabbitAdmin rabbitAdmin = adminMap.get(key);
        rabbitAdmin.declareQueue(testDeviceQueue);

        // binding
        TopicExchange topicExchange = new TopicExchange(exchangeName);
        Binding binding = BindingBuilder.bind(testDeviceQueue).to(topicExchange).with(routingKey);
        rabbitAdmin.declareBinding(binding);

        CachingConnectionFactory connectionFactory = connMap.get(key);
        synchronized (this) {
            Channel channel = connectionFactory.createConnection().createChannel(false);
            long count = channel.consumerCount(testDeviceQueueName);
            System.out.println("######listener2##consumerCount="+count);
            if (count == 0) {
                channel.basicConsume(testDeviceQueueName, new Consumer02(channel));
            }
        }
        return "OK";
    }


    // @GetMapping("/send")
    // public String send() throws Exception{
    //     rabbitTemplate.convertAndSend(exchangeName, routingKey, "dafdasdfa3fsdfsfsdf", new MessagePostProcessor() {
    //         @Override
    //         public Message postProcessMessage(Message message) throws AmqpException {
    //             log.info("##############msg={}", message);
    //             return message;
    //         }
    //     });
    //     return "OK";
    // }
}
