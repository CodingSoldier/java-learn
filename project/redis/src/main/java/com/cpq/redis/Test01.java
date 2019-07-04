package com.cpq.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
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
