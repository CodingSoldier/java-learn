package com.cpq.rabbitmqapi.i_dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

public class Consumer {


    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        // 这就是一个普通的交换机 和 队列 以及路由
        String exchangeName = "test_dlx_exchange";
        String routingKey = "dlx.#";
        String queueName = "test_dlx_queue";

        channel.exchangeDeclare(exchangeName, "topic", true, false, null);

        Map<String, Object> agruments = new HashMap<String, Object>();
        agruments.put("x-dead-letter-exchange", "dlx.exchange");
        /**
         * 这个agruments属性，要设置到正常队列上
         * 队列无法消费消息，则会消息转到agruments中的死信队列交换机dlx.exchange
         */
        channel.queueDeclare(queueName, true, false, false, agruments);
        channel.queueBind(queueName, exchangeName, routingKey);

        /**
         * 声明死信队列交换机dlx.exchange
         * 信息会被路由到dlx.queue
         */
        channel.exchangeDeclare("dlx.exchange", "topic", true, false, null);
        channel.queueDeclare("dlx.queue", true, false, false, null);
        channel.queueBind("dlx.queue", "dlx.exchange", "#");

        //监听正常队列
        channel.basicConsume(queueName, true, new MyConsumer(channel));

        /**
         * 测试步骤
         * 1、先启动Consumer，在rabbitmq中生成交换机、队列。然后关闭Consumer。
         * 2、启动Producer，消息无法被正常队列消费，10s后消息过期，信息会转到死信队列
         * 3、这代码还不完善，还要写个监听监听死信队列dlx.queue
         */

    }
}
