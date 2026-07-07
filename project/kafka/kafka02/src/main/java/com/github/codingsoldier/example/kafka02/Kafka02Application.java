package com.github.codingsoldier.example.kafka02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * kafka02 启动类
 *
 * @author codingsoldier
 */
@SpringBootApplication
@Slf4j
public class Kafka02Application {

  /**
   * 应用入口
   *
   * @param args 启动参数
   */
  public static void main(String[] args) {
    SpringApplication.run(Kafka02Application.class, args);
  }

}
