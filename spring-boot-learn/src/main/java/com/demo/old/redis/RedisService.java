package com.demo.old.redis;

import com.demo.config.AppProp;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
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
    AppProp appProp;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    protected StringRedisTemplate stringRedisTemplate;

    protected HashOperations hashOperations;
    protected SetOperations setOperations;
    protected ValueOperations valueOperations;
    Long redisExpire = 2592000L;

    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.opsForValue();
        hashOperations = redisTemplate.opsForHash();
        setOperations = redisTemplate.opsForSet();
        redisExpire = appProp.getRedisExpire();
    }

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return redisTemplate;
    }

    /*value*/
    public Boolean valueSetObject(String hkey, Object object){
        return valueSetObject(hkey, object, redisExpire);
    }

    public Boolean valueSetObject(String hkey, Object object, long expire){
        redisTemplate.opsForValue().set(hkey, object);
        return redisTemplate.expire(hkey, expire, TimeUnit.SECONDS);
    }

    public T valueGetObject(String hkey, Class<T> clazz){
        return (T)redisTemplate.opsForValue().get(hkey);
    }

    public void valueSetString(String key, String value){
        valueSetString(key, value, redisExpire);
    }

    public void valueSetString(String key, String value, long expire){
        if (StringUtils.isEmpty(value)) {
            return ;
        }
        logger.info("set redis value:" + value);
        valueOperations.set(key, value, expire, TimeUnit.SECONDS);
    }

    public Object valueGetString(String key){
        Object val = valueOperations.get(key);
        if (val != null){
            logger.info("get redis value:" + val.toString());
        }
        return val;
    }

    /*hash*/
    public Boolean hashSetField(String hkey, String field, String value, long expire) throws DataAccessException {
        if (null == value) {
            value = "";
        }
        hashOperations.put(hkey, field, value);  // hkey指向一个hashMap{field: value}
        return redisTemplate.expire(hkey, expire, TimeUnit.SECONDS);
    }

    public Boolean hashSetField(String hkey, String field, String value){
        return hashSetField(hkey, field, value, redisExpire);
    }

    //存储map集合
    public void hashPutAll(String hkey, Map map){
        if (StringUtils.isBlank(hkey)) {
            return;
        }
        hashOperations.putAll(hkey,map);
    }

    //返回map集合
    public Map<String, Object> hashEntries(String hkey){
        return hashOperations.entries(hkey);
    }


    /*set*/
    public void putSet(String skey, Collection<T> value, Long expire) throws DataAccessException {
        if (null == value) {
            return ;
        }
        delete(skey);
        for (T elem:value){
            setOperations.add(skey, elem);
        }
        expire(skey, expire);
    }
    public void putSet(String skey, Collection<T> value) throws DataAccessException {
        if (null == value) {
            return ;
        }
        putSet(skey, value, redisExpire);
    }
    public <T> Set<T> getCollectionSet(String lkey) throws DataAccessException {
        return setOperations.members(lkey);
    }


    /*template*/
    //通过key删除缓存
    public void delete(String key){
        redisTemplate.delete(key);
    }

    //批量删除缓存
    public void delete(Collection<String> key){
        redisTemplate.delete(key);
    }

    //key是否存在
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    //查询条件pattern，查询key集合
    public Set<String> keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    //key剩余时间
    public Long getExpire(String key){
        return redisTemplate.getExpire(key);
    }

    //设置key时间
    public Boolean expire(String key, Long num){
        return redisTemplate.expire(key, num, TimeUnit.SECONDS);
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
