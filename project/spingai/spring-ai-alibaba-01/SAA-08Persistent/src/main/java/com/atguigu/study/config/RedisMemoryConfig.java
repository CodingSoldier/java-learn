package com.atguigu.study.config;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisMemoryConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    public RedisChatMemoryRepository redisChatMemoryRepository(){
        return RedisChatMemoryRepository.builder()
                .host(host)
                .port(port)
                .build();
    }




}
