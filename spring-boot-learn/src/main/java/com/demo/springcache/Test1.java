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
        springCacheService.selectByPrimaryKey("f450a389-fd4b-4a24-87ec-ae4188fccfab");
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
        springCache.setId("acec0fe6-9591-42e3-87f8-c64799b04153");
        springCache.setName("dfgadf333");
        springCache.setNum(new Random().nextInt(100));
        springCache.setIsTrue(new Random().nextInt(100) % 2 == 0);
        springCache.setDate(new Date());
        springCacheService.updateByPrimaryKey(springCache);
    }

    @Test
    public void delete(){
        springCacheService.deleteByPrimaryKey("2b1a3605-0197-455e-98bd-6950006539d4");
    }

    @Test
    public void deleteAll(){
        springCacheService.deleteAllEntries();
    }

}
