package com.demo.springcache.service;

import com.demo.redis.RedisConst;
import com.demo.springcache.mapper.SpringCacheMapper;
import com.demo.springcache.model.SpringCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SpringCacheService {

    @Autowired
    SpringCacheMapper springCacheMapper;

    //通过@来查找bean
    @Cacheable(value = RedisConst.SPRING_CACHE, key = "@redisConst.SPRING_CACHE + #id" )
    public SpringCache selectByPrimaryKey(String id){
        return springCacheMapper.selectByPrimaryKey(id);
    }

    @Transactional
    @CachePut(value = RedisConst.SPRING_CACHE, key = "@redisConst.SPRING_CACHE + #record.getId()")
    //@CachePut(value = RedisConst.SPRING_CACHE)
    public SpringCache insertSelective(SpringCache record){
        springCacheMapper.insertSelective(record);
        record.setName(record.getName()+"名字000");
        //updateByPrimaryKey(record);
        //Integer a=1, b = 0;
        //Integer c = a/b;
        return record;
    }

    @Transactional
    @CachePut(value = RedisConst.SPRING_CACHE, key = "@redisConst.SPRING_CACHE + #record.getId()")
    public SpringCache updateByPrimaryKey(SpringCache record){
        springCacheMapper.updateByPrimaryKey(record);
        return record;
    }

    @CacheEvict(value = RedisConst.SPRING_CACHE, key = "@redisConst.SPRING_CACHE + #id")
    public int deleteByPrimaryKey(String id){
        return springCacheMapper.deleteByPrimaryKey(id);
    }

    @CacheEvict(value = RedisConst.SPRING_CACHE, allEntries=true)
    public void deleteAllEntries(){

    }

}
