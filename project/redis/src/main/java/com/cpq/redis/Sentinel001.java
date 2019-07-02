package com.cpq.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Sentinel001 {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    public void test001() throws Exception{
        while (true){
            String key = "two:" + new Date().getTime();
            redisTemplate.opsForValue().set(key, new Date().getTime());
            TimeUnit.MILLISECONDS.sleep(100L);
            System.out.println(redisTemplate.opsForValue().get(key));
        }

    }

}
