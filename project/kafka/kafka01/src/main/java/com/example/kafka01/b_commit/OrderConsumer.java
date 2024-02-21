package com.example.kafka01.b_commit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.LoggerFactory;

/**
 * @author chenpq05
 * @since 2024/2/19 13:53
 */
public class OrderConsumer {
  public static void main(String[] args) {
    // 设置日志级别
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.INFO);
    // 1、连接集群
    Properties props = new Properties();
    props.put("bootstrap.servers", "127.0.0.1:9092");
    props.put("group.id", "test2");
    //以下两行代码 ---消费者自动提交offset值
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms",  "1000");
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
    // 2、发送数据 发送数据需要，订阅下要消费的topic：order
    kafkaConsumer.subscribe(Arrays.asList("order-p"));
    while (true) {
      // jdk queue offer插入、poll获取元素。 blockingqueue put插入原生， take获取元素
      ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100);
      for (ConsumerRecord<String, String> record : consumerRecords) {
        System.out.println("消费的数据为：" + record.value());
      }
    }
  }
}
