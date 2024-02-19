package com.example.kafka01.b_commit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.LoggerFactory;

/**
 * @author chenpq05
 * @since 2024/2/19 13:53
 */
public class OrderCommitSync02OffsetConsumer {
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
    try {
      while(true) {
        ConsumerRecords<String, String> records = consumer.poll(100);
        for (TopicPartition partition : records.partitions()) {
          List<ConsumerRecord<String, String>> partitionRecords = records.records(partition);
          for (ConsumerRecord<String, String> record : partitionRecords) {
            System.out.println("消费：" + record.offset() + " : " + record.value());
          }
          long lastOffset = partitionRecords.get(partitionRecords.size() - 1).offset();
          consumer.commitSync(Collections.singletonMap(partition, new OffsetAndMetadata(lastOffset + 1)));
        }
      }
    } finally {
      consumer.close();
    }
  }
}
