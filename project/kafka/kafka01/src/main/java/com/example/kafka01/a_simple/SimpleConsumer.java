package com.example.kafka01.a_simple;

import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenpq05
 * @since 2024/2/2 14:00
 */
public class SimpleConsumer {

  public static void main(String[] args) throws Exception {

    // 设置日志级别
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.INFO);

    String topicName = "first";
    Properties props = new Properties();

    props.put("bootstrap.servers", "localhost:9092");
    props.put("group.id", "test");
    props.put("enable.auto.commit", "true");
    props.put("auto.commit.interval.ms", "1000");
    props.put("session.timeout.ms", "30000");
    props.put("key.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");
    props.put("value.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
    // Kafka Consumer subscribes list of topics here.
    consumer.subscribe(Arrays.asList(topicName));

    // print the topic name
    System.out.println("Subscribed to topic " + topicName);
    int i = 0;

    while (true) {
      ConsumerRecords<String, String> records = consumer.poll(100);
      for (ConsumerRecord<String, String> record : records) {
        // print the offset,key and value for the consumer records.
        System.out.printf("offset = %d, key = %s, value = %s\n",
            record.offset(), record.key(), record.value());
      }
    }
  }

}
