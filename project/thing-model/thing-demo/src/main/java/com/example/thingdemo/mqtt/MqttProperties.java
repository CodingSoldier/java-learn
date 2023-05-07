package com.example.thingdemo.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author caich
 * @since 2021/9/15
 * mqtt属性
 */
@Configuration
@ConfigurationProperties("spring.mqtt")
@Data
public class MqttProperties {

  /**
   * 用户名
   */
  private String username;
  /**
   * 密码
   */
  private String password;
  /**
   * 连接地址
   */
  private String hostUrl;
  /**
   * 生产者客户Id
   */
  private String providerClientId;
  /**
   * 消费者客户Id
   */
  private String consumerClientId;
  /**
   * 默认连接话题
   */
  private String defaultTopic;
  /**
   * 超时时间
   */
  private int timeout = 100;
  /**
   * 保持连接数
   */
  private int keepalive = 60;

  /**
   * 入站适配器数量
   */
  //private int inboundCount;

  /**
   * 出站适配器数量
   */
  //private int outboundCount;

  //private int triggerCount;

}
