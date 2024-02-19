package com.example.kafka01.b_commit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.LoggerFactory;

/**
 * @author chenpq05
 * @since 2024/2/19 13:42
 */
public class OrderProducer {
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
    for (int i = 0; i < 3; i++) {

      // 发送数据,需要一个producerRecord对象,最少参数 String topic, V value
      ProducerRecord<String, String> record = new ProducerRecord<>("order", "订单信息！" + i);
      kafkaProducer.send(record);

      ////第一种分区策略，如果既没有指定分区号，也没有指定数据key，那么就会使用轮询的方式将数据均匀的发送到不同的分区里面去
      //ProducerRecord<String, String> producerRecord1 = new ProducerRecord<>("mypartition", "mymessage" + i);
      //kafkaProducer.send(producerRecord1);
      //
      ////第二种分区策略 如果没有指定分区号，指定了数据key，通过key.hashCode  % numPartitions来计算数据究竟会保存在哪一个分区里面
      ////注意：如果数据key，没有变化   key.hashCode % numPartitions  =  固定值  所有的数据都会写入到某一个分区里面去
      //ProducerRecord<String, String> producerRecord2 = new ProducerRecord<>("mypartition", "mykey", "mymessage" + i);
      //kafkaProducer.send(producerRecord2);
      //
      ////第三种分区策略：如果指定了分区号，那么就会将数据直接写入到对应的分区里面去
      //ProducerRecord<String, String> producerRecord3 = new ProducerRecord<>("mypartition", 0, "mykey", "mymessage" + i);
      // kafkaProducer.send(producerRecord3);
      //
      ////第四种分区策略：自定义分区策略。如果不自定义分区规则，那么会将数据使用轮询的方式均匀的发送到各个分区里面去
      //ProducerRecord<String, String> producerRecord4 = new ProducerRecord<>("mypartition", "mymessage" + i);
      //kafkaProducer.send(producerRecord4);

      Thread.sleep(100);
    }
  }
}
