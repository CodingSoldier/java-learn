package com.demo.springcache.service;

import com.demo.redis.RedisConst;
import com.demo.springcache.mapper.SpringCacheMapper;
import com.demo.springcache.model.SpringCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SpringCacheService {

    @Autowired
    RedisConst redisConst;

    @Autowired
    SpringCacheMapper springCacheMapper;

    @Cacheable(value = RedisConst.SPRING_CACHE,  key = "#id" )
    public SpringCache selectByPrimaryKey(String id){
        return springCacheMapper.selectByPrimaryKey(id);
    }

    //@CachePut(value = RedisConst.SPRING_CACHE, keyGenerator = "keyGeneratorCustom")
    @CachePut(value = RedisConst.SPRING_CACHE, key = "#{T(com.demo.redis.RedisConst).SPRING_CACHE+record.getId()}")
    public SpringCache insertSelective(SpringCache record){
        springCacheMapper.insertSelective(record);
        return record;
    }

    @CachePut(value = RedisConst.SPRING_CACHE, key = "#record.getId()")
    public SpringCache updateByPrimaryKey(SpringCache record){
        springCacheMapper.updateByPrimaryKey(record);
        return record;
    }

    @CacheEvict(value = RedisConst.SPRING_CACHE, key = "#id")
    public int deleteByPrimaryKey(String id){
        return springCacheMapper.deleteByPrimaryKey(id);
    }

    @CacheEvict(value = RedisConst.SPRING_CACHE, allEntries=true)
    public void deleteAllEntries(){

    }

}
