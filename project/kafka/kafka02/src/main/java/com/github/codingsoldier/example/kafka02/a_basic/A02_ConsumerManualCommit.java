package com.github.codingsoldier.example.kafka02.a_basic;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * 手动提交 offset 的消费者示例（生产环境主流做法）
 *
 * <p>与 a01 的自动提交相比，本示例做了两处关键改进：</p>
 * <ol>
 *   <li>关闭自动提交，处理完一批消息后再 {@code commitSync()}，保证"先处理后提交"，
 *       语义为至少一次（At Least Once）：崩溃时最多重复消费一批，绝不丢消息。
 *       重复消费由消费端幂等兜底（如数据库唯一键去重）。</li>
 *   <li>通过 ShutdownHook + {@code consumer.wakeup()} 优雅退出：
 *       Ctrl+C 时 poll 会抛出 {@link WakeupException}，循环退出后正常 close，
 *       消费者立即离开消费者组，不必等 session 超时才触发 rebalance。</li>
 * </ol>
 *
 * @author codingsoldier
 */
public class A02_ConsumerManualCommit {

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
   * 消费者组
   */
  private static final String GROUP_ID = "kafka02-a02-manual-group";

  /**
   * 消费者入口，手动提交 offset 并支持优雅退出
   *
   * @param args 启动参数
   */
  public static void main(String[] args) {
    // 1. 构建消费者配置
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    // 关键：关闭自动提交，改为处理完成后手动提交
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    // 没有已提交 offset 时，从最早的消息开始消费
    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

    // 2. 注册 ShutdownHook：Ctrl+C 时唤醒阻塞中的 poll，并等待主线程完成收尾
    final Thread mainThread = Thread.currentThread();
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      System.out.println("收到退出信号，唤醒消费者……");
      // wakeup 是唯一可以跨线程安全调用的 KafkaConsumer 方法
      consumer.wakeup();
      try {
        // 等待主线程走完 finally 中的 close，再让 JVM 退出
        mainThread.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        System.err.println("等待主线程退出被中断：" + e.getMessage());
      }
    }));

    // 3. 订阅主题并循环消费
    try {
      consumer.subscribe(Collections.singletonList(TOPIC));
      System.out.println("已订阅主题：" + TOPIC + "，开始消费（手动提交模式）……");

      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
        if (records.isEmpty()) {
          continue;
        }
        for (ConsumerRecord<String, String> record : records) {
          // 模拟业务处理。生产环境中此处应保证幂等（如按 key 或业务 ID 做数据库唯一键去重），
          // 这样即使"处理完成但提交前崩溃"导致重复消费，也不会产生副作用
          System.out.printf("处理消息 -> topic=%s, partition=%d, offset=%d, key=%s, value=%s%n",
              record.topic(), record.partition(), record.offset(), record.key(), record.value());
        }
        // 关键：整批处理完成后同步提交。commitSync 内部会自动重试可恢复的失败，
        // 遇到不可恢复错误（如已被 rebalance 踢出组）才抛异常
        consumer.commitSync();
        System.out.printf("本批 %d 条消息处理完成，offset 已提交%n", records.count());
      }
    } catch (WakeupException e) {
      // wakeup 触发的正常退出路径，无需处理
      System.out.println("消费者被唤醒，准备退出");
    } finally {
      // close 内部会先提交（如有配置）并主动离开消费者组，触发即时 rebalance
      consumer.close();
      System.out.println("消费者已关闭");
    }
  }

}
