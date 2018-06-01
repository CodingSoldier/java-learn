package com.demo.springcache;

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
public class Test1 {

    @Autowired
    SpringCacheService springCacheService;

    @Test
    public void get(){
        SpringCache sc = springCacheService.selectByPrimaryKey("2626218d-c370-4817-be18-e6c60d096268");
        System.out.println(sc.toString());
    }

    @Test
    public void insert(){
        SpringCache springCache = new SpringCache();
        springCache.setId(UUID.randomUUID().toString());
        springCache.setName("name1111");
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
