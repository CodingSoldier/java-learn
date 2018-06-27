package com.demo.springcache;

import com.demo.redis.RedisService;
import com.demo.springcache.model.SpringCache;
import com.demo.springcache.service.MapService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMap {

    @Autowired
    MapService mapService;

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
        Map<String, String> sc = mapService.selectByPrimaryKey("4348b272-ec6f-4f7d-9ccf-bcaab5fad154");
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
        mapService.insertSelective(springCache);
    }

    @Test
    public void update(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", "2626218d-c370-4817-be18-e6c60d096268");
        map.put("name", "nnnnnnnnnnnn111111111");
        map.put("num", new Random().nextInt(100));
        map.put("isTrue", new Random().nextInt(100) % 2 == 0);
        map.put("date", new Date());
        mapService.updateByPrimaryKey(map);
    }

    @Test
    public void delete(){
        mapService.deleteByPrimaryKey("2626218d-c370-4817-be18-e6c60d096268");
    }

    @Test
    public void deleteAll(){
        mapService.deleteAllEntries();
    }

}
