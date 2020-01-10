//package com.cpq.shiro2.cache;
//
//import com.cpq.shiro2.user.entity.UserVo;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.shiro.cache.Cache;
//import org.apache.shiro.cache.CacheException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.Collection;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author chengchenrui
// * @version Id: RedisCache.java, v 0.1 2018/6/29 1:15 chengchenrui Exp $$
// */
//@SuppressWarnings("unchecked")
//@Component
//public class RedisCache<K, V> implements Cache<K, V> {
//
//    @Autowired
//    private RedisTemplate<String, V> redisTemplate;
//
//    @Override
//    public V get(K k) throws CacheException {
//        System.out.println("从Redis中获取权限数据");
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // k是AuthRealm
//        UserVo userVo = new UserVo();
//        try {
//            Object primaryPrincipal = objectMapper.readValue(objectMapper.writeValueAsString(k), Map.class).get("primaryPrincipal");
//            userVo = objectMapper.readValue(objectMapper.writeValueAsString(primaryPrincipal), UserVo.class);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return redisTemplate.opsForValue().get(userVo.getUid().toString());
//    }
//
//    @Override
//    public V put(K k, V v) throws CacheException {
//        // k是AuthRealm
//        UserVo userVo = new UserVo();
//
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            Object primaryPrincipal = objectMapper.readValue(objectMapper.writeValueAsString(k), Map.class).get("primaryPrincipal");
//            userVo = objectMapper.readValue(objectMapper.writeValueAsString(primaryPrincipal), UserVo.class);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//        // 这里又没法序列化
//        redisTemplate.opsForValue().set(userVo.getUid().toString(), objectMapper.writeValueAsString(v));
//        redisTemplate.expire(userVo.getUid().toString(), 1, TimeUnit.HOURS);
//        return v;
//    }
//
//    @Override
//    public V remove(K k) throws CacheException {
//        UserVo userVo = new UserVo();
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            Object primaryPrincipal = objectMapper.readValue(objectMapper.writeValueAsString(k), Map.class).get("primaryPrincipal");
//            userVo = objectMapper.readValue(objectMapper.writeValueAsString(primaryPrincipal), UserVo.class);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        V value = redisTemplate.opsForValue().get(userVo.getUid().toString());
//        redisTemplate.delete(userVo.getUid().toString());
//        return value;
//    }
//
//    @Override
//    public void clear() throws CacheException {
//
//    }
//
//    @Override
//    public int size() {
//        return 0;
//    }
//
//    @Override
//    public Set<K> keys() {
//        return null;
//    }
//
//    @Override
//    public Collection<V> values() {
//        return null;
//    }
//
//}