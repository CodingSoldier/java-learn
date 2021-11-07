package com.cpq.mqspring;

import com.cpq.mqspring.convert.ImageMessageConverter;
import com.cpq.mqspring.convert.PDFMessageConverter;
import com.cpq.mqspring.convert.TextMessageConverter;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
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


        //MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //adapter.setDefaultListenerMethod("consumeMessage");
        ///**
        // * 加一个转换器，TextMessageConverter转化器中
        // * message.getMessageProperties().getContentType().contains("text")则把消息转为String
        // * 使用testTextMessageConverter()测试
        // * 最终MessageDelegate中的consumeMessage(String message)接收到消息
        // */
        //adapter.setMessageConverter(new TextMessageConverter());
        //container.setMessageListener(adapter);

        ////queue和MessageDelegate中的方法绑定
        //MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //adapter.setMessageConverter(new TextMessageConverter());
        //Map<String, String> queueOrTagToMethodName = new HashMap<>();
        //queueOrTagToMethodName.put("queue001", "method1");
        //queueOrTagToMethodName.put("queue002", "method2");
        //adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
        //container.setMessageListener(adapter);


        /**
         * json转换器，E_Test.testJackson2JsonMessageConverter发送json字符串byte[]
         * MessageDelegate中的consumeMessage(Map messageBody)会接收到消息
         */
        //MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //adapter.setDefaultListenerMethod("consumeMessage");
        //adapter.setMessageConverter(new Jackson2JsonMessageConverter());
        //container.setMessageListener(adapter);


        /**
         * java类转换器
         * E_Test#testJavaTypeMapper()发送，MessageDelegate#consumeMessage(Order order)接收
         */
        //MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //adapter.setDefaultListenerMethod("consumeMessage");
        //Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        //DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        //jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        //adapter.setMessageConverter(new Jackson2JsonMessageConverter());
        //container.setMessageListener(adapter);


        /**
         * 转换器配置多个java类的映射
         * 运行testJavaTypeMapperMore()，MessageDelegate.consumeMessage(Order order)、consumeMessage(Packaged pack)接收到数据
         */
        //MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        //adapter.setDefaultListenerMethod("consumeMessage");
        //Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        //DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
        //Map<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
        //idClassMapping.put("order", com.cpq.mqspring.entity.Order.class);
        //idClassMapping.put("packaged", com.cpq.mqspring.entity.Packaged.class);
        //javaTypeMapper.setIdClassMapping(idClassMapping);
        //jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
        //adapter.setMessageConverter(jackson2JsonMessageConverter);
        //container.setMessageListener(adapter);


        /**
         * 全局转换器
         */
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");
        //全局的转换器:
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();
        //text转换器
        TextMessageConverter textConvert = new TextMessageConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);
        //json转换器
        Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
        convert.addDelegate("json", jsonConvert);
        convert.addDelegate("application/json", jsonConvert);
        //image转化器
        ImageMessageConverter imageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png", imageConverter);
        convert.addDelegate("image", imageConverter);
        //pdf转化器
        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        convert.addDelegate("application/pdf", pdfConverter);

        adapter.setMessageConverter(convert);
        container.setMessageListener(adapter);

        return container;
    }
}
