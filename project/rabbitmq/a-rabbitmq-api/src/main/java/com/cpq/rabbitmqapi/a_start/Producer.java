package com.cpq.rabbitmqapi.a_start;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {
    public static void main(String[] args) throws Exception {

        //1 创建一个ConnectionFactory, 并进行配置
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("192.168.40.129");
        connectionFactory.setPort(5672);
        connectionFactory.setVirtualHost("/");

        //2 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();

        //3 通过connection创建一个Channel
        Channel channel = connection.createChannel();

        //4 通过Channel发送数据
        for (int i = 0; i < 5; i++) {
            String msg = "Hello RabbitMQ! " + i;
            System.out.println("生产端：" + msg);
            /** basicPublish
             * 第一个参数是Exchanges交换机，不指定交换机，就使用AMQP default，默认路由绑定到每一个队列
             * 第二个参数是routing-key，交换机为AMQP default且指定routing-key时，routing-key消息路由到具有相同名称的queue中
             */

            channel.basicPublish("", "test001", null, msg.getBytes());
        }

        //5 记得要关闭相关的连接
        channel.close();
        connection.close();
    }
}
