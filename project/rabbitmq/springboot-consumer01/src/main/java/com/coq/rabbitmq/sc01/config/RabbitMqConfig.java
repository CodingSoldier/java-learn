package com.coq.rabbitmq.sc01.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class RabbitMqConfig {

    /**
     * 一个错误处理函数，当{code @RabbitListener}方法抛出异常时被调用。它在堆栈中比侦听器容器的错误处理程序更高的位置被调用。
     */
    @Bean
    public RabbitListenerErrorHandler customListenerErrorHandler(){
        return new RabbitListenerErrorHandler() {
            @Override
            public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {
                Object payload = message.getPayload();
                ObjectMapper objectMapper = new ObjectMapper();
                String playloadStr = objectMapper.writeValueAsString(payload);
                System.out.println("customListenerErrorHandler，playloadStr: " + playloadStr);

                log.error("", exception);
                /**
                 * 需要抛出异常，在ack=auto的场景下，不抛出异常，不会重试
                 */
                throw exception;
            }
        };
    }
}
