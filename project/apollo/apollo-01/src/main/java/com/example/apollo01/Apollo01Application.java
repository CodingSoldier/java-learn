package com.example.apollo01;

import com.ctrip.framework.apollo.spring.annotation.EnableApolloConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableApolloConfig
public class Apollo01Application {
  public static void main(String[] args) {
    SpringApplication.run(Apollo01Application.class, args);
  }

}
