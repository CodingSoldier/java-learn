package com.example.kafka01.d_callback;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.Properties;
import java.util.concurrent.Future;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.LoggerFactory;

/**
 * @author chenpq05
 * @since 2024/2/19 13:42
 */
public class CallbackProducer {
  public static void main(String[] args) throws InterruptedException {
    // 设置日志级别
    Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    root.setLevel(Level.INFO);
    /* 1、连接集群，通过配置文件的方式
     * 2、发送数据-topic:order，value
     */
    Properties props = new Properties();
    props.put("bootstrap.servers", "127.0.0.1:9092");
    props.put("acks", "all");
    props.put("retries", 0);
    props.put("batch.size", 16384);
    props.put("linger.ms", 1);
    props.put("buffer.memory", 33554432);
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
    for (int i = 0; i < 30; i++) {
      // 发送数据,需要一个producerRecord对象,最少参数 String topic, V value
      ProducerRecord<String, String> record = new ProducerRecord<>("order-p", "订单信息！" + i);
      Future<RecordMetadata> future = kafkaProducer.send(record, new DemoProducerCallback());
      Thread.sleep(100);
    }
  }
}
