package com.example.kafka01.b_commit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.LoggerFactory;

/**
 * @author chenpq05
 * @since 2024/2/19 13:53
 */
public class OrderCommitSyncConsumer {
  public static void main(String[] args) {
    // 设置日志级别
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.INFO);

    Properties props = new Properties();
    props.put("bootstrap.servers", "127.0.0.1:9092");
    props.put("group.id", "test");
//关闭自动提交确认选项
    props.put("enable.auto.commit", "false");
    props.put("key.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("order"));
    final int minBatchSize = 6;
    List<ConsumerRecord<String, String>> buffer = new ArrayList<>();
    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(100);
      for (ConsumerRecord<String, String> record : records) {
        buffer.add(record);
      }
      if (buffer.size() >= minBatchSize) {
        buffer.forEach(e -> System.out.println("消费："+e.value()));
        // 手动提交offset值
        consumer.commitSync();
        buffer.clear();
      }
    }
  }
}
