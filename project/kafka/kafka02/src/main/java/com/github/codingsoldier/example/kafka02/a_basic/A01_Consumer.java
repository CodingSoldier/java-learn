package com.github.codingsoldier.example.kafka02.a_basic;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Kafka 集群消费者示例
 *
 * <p>连接 教程.md ## 集群 中描述的三 Broker 集群，订阅 Topic {@code kafka.a01.test} 并持续消费消息。</p>
 *
 * @author codingsoldier
 */
public class A01_Consumer {

  /**
   * 集群 Bootstrap Servers，三个 Broker
   */
  private static final String BOOTSTRAP_SERVERS =
      "192.168.1.221:9092,192.168.1.221:9093,192.168.1.221:9094";

  /**
   * 消息主题
   */
  private static final String TOPIC = "kafka.a01.test";

  /**
   * 消费者组
   */
  private static final String GROUP_ID = "kafka02-a01-group";

  /**
   * 消费者入口，持续轮询并打印集群消息
   *
   * @param args 启动参数
   */
  public static void main(String[] args) {
    // 1. 构建消费者配置
    Properties props = new Properties();
    // 集群地址
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    // 消费者组
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    // 自动提交 offset
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
    props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
    // 没有已提交 offset 时，从最早的消息开始消费
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    // key、value 反序列化器
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    // 2. 创建消费者，订阅主题并循环消费（try-with-resources 自动关闭）
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
      consumer.subscribe(Collections.singletonList(TOPIC));
      System.out.println("已订阅主题：" + TOPIC + "，开始消费……");

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        for (ConsumerRecord<String, String> record : records) {
          System.out.printf("收到消息 -> topic=%s, partition=%d, offset=%d, key=%s, value=%s%n",
              record.topic(), record.partition(), record.offset(), record.key(), record.value());
        }
      }
    }
  }

}
