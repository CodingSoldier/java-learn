package com.cpq.mqspring;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class D_SimpleMessageListenerContainer {

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
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
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
        container.setMessageListener(new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msg = new String(message.getBody());
                System.err.println("消费者  " + msg);
            }
        });
        return container;
    }

}
