package com.coq.rabbitmq.sp01.config;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRabbitmqConfig {

    // @Bean(name = "rabbitTemplate")
    // RabbitTemplate rabbitTemplate(){
    //     return new RabbitTemplate();
    // }

    /**
     * 设置回调
     * @param connectionFactory
     * @return
     */
    @Bean(name = "callbackRabbitTemplate")
    RabbitTemplate callbackRabbitTemplate(ConnectionFactory connectionFactory){
        /**
         * rabbitmq 整个消息投递的路径为：
         * producer->rabbitmq broker cluster->exchange->queue->consumer
         *
         * message 从 producer 到 rabbitmq broker cluster 则会返回一个 confirmCallback
         * message 从 exchange->queue 投递失败则会返回一个 returnCallback 。
         */
        final RabbitTemplate.ConfirmCallback confirmCallback= new RabbitTemplate.ConfirmCallback(){
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause){
                System.out.println("ConfirmCallback##correlationData "+correlationData.toString());
                System.out.println("ConfirmCallback##ack " +ack);
                System.out.println("ConfirmCallback##cause " +cause);
                if (!ack){
                    System.out.println("1、没找到交换机。2、有交换机但是交换机没有routingKey。异常处理");
                }
            }
        };

        // 回调函数: return返回
        // 有交换机且有routingKey，但是routingKey路由不到队列
        final RabbitTemplate.ReturnCallback returnCallback = new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(org.springframework.amqp.core.Message message, int replyCode, String replyText, String exchange, String routingKey) {
                System.err.println("return exchange: " + exchange + ", routingKey: "
                        + routingKey + ", replyCode: " + replyCode + ", replyText: " + replyText);
            }
        };

        RabbitTemplate callbackRabbitTemplate = new RabbitTemplate(connectionFactory);
        callbackRabbitTemplate.setConfirmCallback(confirmCallback);
        callbackRabbitTemplate.setReturnCallback(returnCallback);

        return callbackRabbitTemplate;
    }

    /**
     * 创建MessagePostProcessor，在对MessageProperties增强
     * @return
     */
    public static MessagePostProcessor createMessagePostProcess(){
        return new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                /**
                 * 消费者使用 messaging.Message#headers也可以获取
                 * postProcessMessage(Message message)中增强的值
                 */
                // 添加重回队列次数
                message.getMessageProperties().getHeaders().put("return-queues-times", 0);
                return message;
            }
        };
    }
}
