package com.demo.springcache.service;

import com.demo.springcache.mapper.SpringCacheMapper;
import com.demo.springcache.model.SpringCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SpringCacheService {

    //public static final String SPRING_CACHE_ID =

    @Autowired
    SpringCacheMapper springCacheMapper;

    @Cacheable(value = "springCache", key = "#id")
    public SpringCache selectByPrimaryKey(String id){
        return springCacheMapper.selectByPrimaryKey(id);
    }

    @CachePut(value = "springCache", key = "#record.getId()")
    public SpringCache insertSelective(SpringCache record){
        springCacheMapper.insertSelective(record);
        return record;
    }

    @CachePut(value = "springCache", key = "#record.getId()")
    public SpringCache updateByPrimaryKey(SpringCache record){
        springCacheMapper.updateByPrimaryKey(record);
        return record;
    }

    @CacheEvict(value = "springCache", key = "#record.getId()")
    public int deleteByPrimaryKey(String id){
        return springCacheMapper.deleteByPrimaryKey(id);
    }

    @CacheEvict(value = "springCache", allEntries=true)
    public void deleteAllEntries(String id){

    }

}
