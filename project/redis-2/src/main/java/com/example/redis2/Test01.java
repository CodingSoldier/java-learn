package com.example.redis2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class Test01 {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test001(){
        while (true){
            try {
                String key = "test:" + new Date().getTime();
                redisTemplate.opsForValue().set(key, new Date().getTime());
                TimeUnit.MILLISECONDS.sleep(100L);
                System.out.println(redisTemplate.opsForValue().get(key));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
