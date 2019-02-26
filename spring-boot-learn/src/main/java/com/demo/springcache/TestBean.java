package com.demo.springcache;

import com.demo.old.redis.RedisService;
import com.demo.springcache.model.SpringCache;
import com.demo.springcache.service.SpringCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBean {

    @Autowired
    SpringCacheService springCacheService;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisService redisService;



    //注解，get
    @Test
    public void get(){
        SpringCache sc = springCacheService.selectByPrimaryKey("4348b272-ec6f-4f7d-9ccf-bcaab5fad154");
        System.out.println(sc.toString());
    }

    @Test
    public void insert(){
        SpringCache springCache = new SpringCache();
        springCache.setId(UUID.randomUUID().toString());
        springCache.setName("123");
        springCache.setNum(new Random().nextInt(100));
        springCache.setDate(new Date());
        springCacheService.insertSelective(springCache);
    }

    @Test
    public void update(){
        SpringCache springCache = new SpringCache();
        springCache.setId("2626218d-c370-4817-be18-e6c60d096268");
        springCache.setName("nnnnnnnnnnnn111111111");
        springCache.setNum(new Random().nextInt(100));
        springCache.setDate(new Date());
        springCacheService.updateByPrimaryKey(springCache);
    }

    @Test
    public void delete(){
        springCacheService.deleteByPrimaryKey("2626218d-c370-4817-be18-e6c60d096268");
    }

    @Test
    public void deleteAll(){
        springCacheService.deleteAllEntries();
    }

}
