package com.example.redis2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class Redisson01 {

    @Autowired
    RedisTemplate redisTemplate;

    @Test
    void test3() {
        // new RedisCallback<>()
        redisTemplate.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.opsForValue().set("aaa", "value");
                redisOperations.expire("aaa", 30, TimeUnit.SECONDS);
                redisOperations.opsForValue().set("bbb", "value-bbb");
                redisOperations.expire("bbb", 40, TimeUnit.SECONDS);
                return null;
            }
        });
    }

}
