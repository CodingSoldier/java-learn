package com.demo.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by admin on 2018/1/22.
 */
@Component
public class RedisService<T> {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    protected HashOperations<Object, String, Object> hashOperations;
    protected ValueOperations<Object, Object> valueOperations;

    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.opsForValue();
        hashOperations = redisTemplate.opsForHash();
    }

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return redisTemplate;
    }

    // 没用到
    public Object hgetAll(String key, String filed) {
        return hashOperations.get(key, filed);
    }

    @SuppressWarnings("unchecked")
    //通过 com.demo.springcache.TestBean#hgetAllKeys
    public Set<String> hgetAllKeys(String hkey) throws DataAccessException {
        return hashOperations.keys(hkey);
    }

    //通过 com.demo.springcache.TestBean#hset
    public void hset(String hkey, String field, String value, long expire) throws DataAccessException {
        if (null == value) {
            value = "";
        }
        hashOperations.put(hkey, field, value);  // hkey指向一个hashMap{field: value}
        if (expire != -1) {
            redisTemplate.expire(hkey, expire, TimeUnit.SECONDS);
        }
    }

    //通过  com.demo.redis.RedisService#hsetForObject
    public void hsetForObject(String hkey, Object object, long expire){
        redisTemplate.opsForValue().set(hkey, object);
        if (expire != -1) {
            redisTemplate.expire(hkey, expire, TimeUnit.SECONDS);
        }
    }

    //通过  com.demo.redis.RedisService#hgetAllForObject
    public T hgetAllForObject(String hkey, Class<T> clazz){
        return (T)redisTemplate.opsForValue().get(hkey);
    }


    // 没用到
    public void hremove(String hkey, String field) throws DataAccessException {
        hashOperations.delete(hkey, field);
    }

    // 通过 com.demo.springcache.TestBean#delete1
    public void delete(String key) throws DataAccessException {
        redisTemplate.delete(key);
    }

    // 通过 com.demo.springcache.TestBean#hentries
    public Map<String, Object> hentries(String key) throws DataAccessException {
        return hashOperations.entries(key);
    }

    //通过 com.demo.springcache.TestBean#setString
    public void setString(String key, String value, long expire) throws DataAccessException {
        logger.info("set redis value:" + value);
        valueOperations.set(key, value, expire, TimeUnit.SECONDS);
    }

    // 通过  com.demo.springcache.TestBean#getString
    public Object getString(String key) throws DataAccessException {
        Object val = valueOperations.get(key);

        if (val != null) {
            logger.info("get redis value:" + val.toString());
            return val.toString().trim();
        }

        return val;
    }

    // 通过 com.demo.springcache.TestBean#set
    public void set(String key, String value, long expire) throws DataAccessException {
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    // 通过 com.demo.springcache.TestBean#getstr
    public Object get(String key) throws DataAccessException {
        Object val = stringRedisTemplate.opsForValue().get(key);
        if (val != null) {
            return val.toString().trim();
        }
        return val;
    }
}
