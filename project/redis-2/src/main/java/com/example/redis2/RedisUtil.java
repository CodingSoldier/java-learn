package com.example.redis2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class RedisUtil{

    private static RedisTemplate staticRedisTemplate;
    private static ValueOperations valueOperations;

    @Autowired
    RedisTemplate redisTemplate;


    @PostConstruct
    public void init(){
        staticRedisTemplate = redisTemplate;
        valueOperations = redisTemplate.opsForValue();
    }

    public static Boolean setIfAbsent(String key, Object value, Duration duration){
        return valueOperations.setIfAbsent(key, value, duration);
    }

    public static Boolean setIfAbsent(String key, Object value){
        return valueOperations.setIfAbsent(key, value);
    }

}
