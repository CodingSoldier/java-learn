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

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    // com.cyyz.spt.platform.common.redis.RedisService#hgetAllKeys
    @Test
    public void hgetAllKeys() throws Exception{
        // keys 返回hash集中所有字段（fields）的名称
        Set<String> keySet = redisTemplate.opsForHash().keys("redisHash");
        System.out.println(keySet);
    }

    // com.cyyz.spt.platform.common.redis.RedisService#hset
    @Test
    public void hset() throws Exception{
        redisTemplate.opsForHash().put("redisHash","name11","aaa11");
        //redisTemplate.opsForHash().put("redisHash1","name1","tom1");
        //redisTemplate.expire("redisHash1", 60, TimeUnit.SECONDS);
    }

    // com.cyyz.spt.platform.common.redis.RedisService#delete
    @Test
    public void delete1() throws Exception{
        redisTemplate.delete("MAP:36bfbe87-7ec6-4078-b0cc-dcbeefb82999");
    }

    // com.cyyz.spt.platform.common.redis.RedisService#hentries
    @Test
    public void hentries() throws Exception{
        //map操作，获取key中所有的field、value
        Map<String, Object> map = redisTemplate.opsForHash().entries("SPRING:CACHE:0ad03de6-93f0-4535-8077-05e8a37eedc5");
        System.out.println(map.toString());
    }

    // com.cyyz.spt.platform.common.redis.RedisService#setString
    @Test
    public void setString() throws Exception{
        redisTemplate.opsForValue().set("string_key", "string_val", 60, TimeUnit.SECONDS);
        redisTemplate.opsForValue().set("string_key1", "string_val1");
    }

    // com.cyyz.spt.platform.common.redis.RedisService#getString
    @Test
    public void getString() throws Exception{
        System.out.println(redisTemplate.opsForValue().get("string_key1"));
    }

    // com.cyyz.spt.platform.common.redis.RedisService#getString
    @Test
    public void set() throws Exception{
        stringRedisTemplate.opsForValue().set("string_key1", "string_val11");
    }

    // com.cyyz.spt.platform.common.redis.RedisService#get
    @Test
    public void getstr() throws Exception{
        System.out.println(stringRedisTemplate.opsForValue().get("string_key1"));
    }

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
