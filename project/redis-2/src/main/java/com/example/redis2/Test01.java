package com.example.redis2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class Test01 {

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    public void test001() {
        while (true) {
            try {
                String key = "test:" + new Date().getTime();
                redisTemplate.opsForValue().set(key, new Date().getTime());
                TimeUnit.MILLISECONDS.sleep(100L);
                System.out.println(redisTemplate.opsForValue().get(key));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test002() throws Exception {
        // String key = "test:" + new Date().getTime();
        // redisTemplate.opsForValue().set(key, new Date().getTime());
        // TimeUnit.MILLISECONDS.sleep(100L);
        // System.out.println(redisTemplate.opsForValue().get(key));

        long start = System.currentTimeMillis();
        redisTemplate.executePipelined(new SessionCallback<Object>() {
            @Override
            @Nullable
            public Object execute(RedisOperations operations) throws DataAccessException {
                String key = "test-key:";
                String val = "test-val:";
                for (int j = 0; j < 10000; j++) {
                    operations.opsForValue().set(key + j, val + j);
                }
                return null;
            }
        });

        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - start));
    }

    @Test
    public void test003() throws Exception {
        long start = System.currentTimeMillis();
        List<Object> objects = redisTemplate.executePipelined(new SessionCallback() {
            @Override
            @Nullable
            public Object execute(RedisOperations operations) throws DataAccessException {
                String key = "test-key:";
                ArrayList<String> list = new ArrayList<>();
                for (int j = 0; j < 10000; j++) {
                    operations.opsForValue().get(key + j);
                    // list.add(String.valueOf(o));
                }
                return null;
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - start));

        System.out.println(objects.toString());
    }

    @Test
    public void test004() throws Exception {
        long start = System.currentTimeMillis();
        String key = "test-key:";
        String val = "test-val:";
        for (int j = 0; j < 10000; j++) {
            redisTemplate.opsForValue().set(key + j, val + j);
        }
        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - start));
    }

    @Test
    public void test05() throws Exception {
        long start = System.currentTimeMillis();
        String key = "test-key:";
        for (int j = 0; j < 10000; j++) {
            redisTemplate.opsForValue().get(key + j);
        }
        long end = System.currentTimeMillis();
        System.out.println("时间：" + (end - start));
    }


}
