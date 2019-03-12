package com.cpq.mqspring;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class E_MessageListenerAdapter {

    @Autowired
    Queue queue001;
    @Autowired
    Queue queue002;
    @Autowired
    Queue queue003;
    @Autowired
    Queue queue_image;
    @Autowired
    Queue queue_pdf;

    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.addQueues(queue001, queue002, queue003, queue_image, queue_pdf);
        container.setConcurrentConsumers(1);
        container.setMaxConcurrentConsumers(5);
        container.setDefaultRequeueRejected(false);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        container.setExposeListenerChannel(true);
        //设置一个tag标签
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });

        /**
         * 先Application运行系统，然后运行测试类
         * 接收到消息后默认会执行MessageDelegate中的handleMessage(byte[] messageBody)
         */
        //MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //container.setMessageListener(adapter);

        //自定义一个方法名consumeMessage，参数不变
        //MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //adapter.setDefaultListenerMethod("consumeMessage");
        //container.setMessageListener(adapter);


        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        /**
         * 加一个转换器，TextMessageConverter转化器中
         * message.getMessageProperties().getContentType().contains("text")则把消息转为String
         * 使用testTextMessageConverter()测试
         * 最终MessageDelegate中的consumeMessage(String message)接收到消息
         */
        adapter.setMessageConverter(new TextMessageConverter());
        container.setMessageListener(adapter);

        return container;
    }
}
