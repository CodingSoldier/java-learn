package com.example.kafka01.d_callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * @author chenpq05
 * @since 2024/2/20 10:17
 */
@Slf4j
public class DemoProducerCallback implements Callback {

  @Override
  public void onCompletion(RecordMetadata recordMetadata, Exception e) {
    log.info("发送消息回调recordMetadata={}", recordMetadata);
    if (e != null) {
      log.error("发送消息异常", e);
    }
  }

}
