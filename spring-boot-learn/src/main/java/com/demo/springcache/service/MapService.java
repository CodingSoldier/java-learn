package com.demo.springcache.service;

import com.demo.redis.RedisConst;
import com.demo.springcache.mapper.SpringCacheMapper;
import com.demo.springcache.model.SpringCache;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class MapService {

    @Autowired
    SpringCacheMapper springCacheMapper;

    //通过@来查找bean
    @Cacheable(value = RedisConst.MAP, key = "@redisConst.MAP + #id" )
    public Map<String, String> selectByPrimaryKey(String id) {
        SpringCache springCache = springCacheMapper.selectByPrimaryKey(id);
        Map<String, String> map = new HashMap<>();
        try {
            map = BeanUtils.describe(springCache);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @Transactional
    @CachePut(value = RedisConst.MAP, key = "@redisConst.MAP + #record.getId()")
    //@CachePut(value = RedisConst.MAP)
    public Map<String, String> insertSelective(SpringCache record){
        springCacheMapper.insertSelective(record);

        Map<String, String> map = new HashMap<>();
        try {
            BeanUtils.copyProperties(map,record);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @Transactional
    @CachePut(value = RedisConst.MAP, key = "@redisConst.MAP + #record.getId()")
    public Map<String, Object> updateByPrimaryKey(Map<String, Object> map){
        SpringCache record = new SpringCache();
        try {
            BeanUtils.copyProperties(record, map);
        }catch (Exception e){

        }
        springCacheMapper.updateByPrimaryKey(record);
        return map;
    }

    @CacheEvict(value = RedisConst.MAP, key = "@redisConst.MAP + #id")
    public int deleteByPrimaryKey(String id){
        return springCacheMapper.deleteByPrimaryKey(id);
    }

    @CacheEvict(value = RedisConst.MAP, allEntries=true)
    public void deleteAllEntries(){

    }

}
