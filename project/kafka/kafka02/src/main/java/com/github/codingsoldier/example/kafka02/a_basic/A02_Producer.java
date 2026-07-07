package com.github.codingsoldier.example.kafka02.a_basic;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

/**
 * a02 生产者示例
 *
 * <p>向 Topic {@code kafka.a02.test} 发送 10 条消息，供 a02 的两个消费者示例消费。
 * kafka-clients 3.0+ 默认开启幂等（enable.idempotence=true、acks=all、retries=Integer.MAX_VALUE），
 * 因此这里不再显式覆盖 acks、retries，保留默认的最高可靠性。</p>
 *
 * @author codingsoldier
 */
public class A02_Producer {

  /**
   * 集群 Bootstrap Servers，三个 Broker
   */
  private static final String BOOTSTRAP_SERVERS =
      "192.168.1.221:9092,192.168.1.221:9093,192.168.1.221:9094";

  /**
   * 消息主题
   */
  private static final String TOPIC = "kafka.a02.test";

  /**
   * 生产者入口，向集群发送 10 条消息
   *
   * @param args 启动参数
   */
  public static void main(String[] args) {
    // 1. 构建生产者配置
    Properties props = new Properties();
    // 集群地址
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    // key、value 序列化器
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // 2. 创建生产者并发送消息（try-with-resources 自动关闭）
    try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
      for (int i = 0; i < 10; i++) {
        String key = "key-" + i;
        String value = "value-" + i;
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key, value);
        // 异步发送并通过回调打印元数据
        final int index = i;
        producer.send(record, (metadata, exception) -> {
          if (exception != null) {
            System.err.printf("第 %d 条消息发送失败：%s%n", index, exception.getMessage());
          } else {
            System.out.printf("发送成功 -> topic=%s, partition=%d, offset=%d, key=%s%n",
                metadata.topic(), metadata.partition(), metadata.offset(), key);
          }
        });
      }
      // 阻塞等待所有未完成的消息发送出去
      producer.flush();
      System.out.println("全部消息发送完成");
    }
  }

}
