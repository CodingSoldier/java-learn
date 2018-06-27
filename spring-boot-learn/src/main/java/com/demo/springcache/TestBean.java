package com.demo.springcache;

import com.demo.redis.RedisService;
import com.demo.springcache.model.SpringCache;
import com.demo.springcache.service.SpringCacheService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    RedisService redisService;

    //redisService方法，get
    @Test
    public void get01() throws Exception{
        SpringCache sc = (SpringCache)redisService.hgetAllForObject("SPRING:CACHE:0c1244c3-8428-4b07-aa83-49ee882810d4", SpringCache.class);
        System.out.println(sc.toString());
    }

    //redisService方法，set
    @Test
    public void insert01() throws Exception{
        String uuid = UUID.randomUUID().toString();
        SpringCache springCache = new SpringCache();
        springCache.setId(uuid);
        springCache.setName("1111");
        springCache.setNum(new Random().nextInt(100));
        springCache.setIsTrue(new Random().nextInt(100) % 2 == 0);
        springCache.setDate(new Date());
        redisService.hsetForObject("SPRING:CACHE:"+uuid, springCache, 1);
    }

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
        springCache.setName("0000");
        springCache.setNum(new Random().nextInt(100));
        springCache.setIsTrue(new Random().nextInt(100) % 2 == 0);
        springCache.setDate(new Date());
        springCacheService.insertSelective(springCache);
    }

    @Test
    public void update(){
        SpringCache springCache = new SpringCache();
        springCache.setId("2626218d-c370-4817-be18-e6c60d096268");
        springCache.setName("nnnnnnnnnnnn111111111");
        springCache.setNum(new Random().nextInt(100));
        springCache.setIsTrue(new Random().nextInt(100) % 2 == 0);
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
