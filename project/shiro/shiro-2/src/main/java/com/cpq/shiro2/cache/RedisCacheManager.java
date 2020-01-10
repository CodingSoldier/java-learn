//package com.cpq.shiro2.cache;
//
//import org.apache.shiro.cache.Cache;
//import org.apache.shiro.cache.CacheException;
//import org.apache.shiro.cache.CacheManager;
//import org.springframework.data.redis.cache.RedisCache;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//
///**
// * @author chengchenrui
// * @version Id: RedisCacheManager.java, v 0.1 2018/6/29 1:13 chengchenrui Exp $$
// */
//@Component
//public class RedisCacheManager implements CacheManager {
//
//    @Resource
//    private RedisCache redisCache;
//
//    @Override
//    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
//        return redisCache;
//    }
//}