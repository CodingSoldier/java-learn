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
        //entityClass = GenericsUtil.getSuperClassGenricType(RedisService.class, 0);
        //redisTemplate.setKeySerializer(new StringRedisSerializer());

//        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<T>(entityClass));
//        redisTemplate.setValueSerializer(new StringRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        valueOperations = redisTemplate.opsForValue();
        hashOperations = redisTemplate.opsForHash();
    }

    public RedisTemplate<Object, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public Object hgetAll(String key, String filed) {
        return hashOperations.get(key, filed);
    }

    @SuppressWarnings("unchecked")
    public Set<String> hgetAllKeys(String hkey) throws DataAccessException {
        return hashOperations.keys(hkey);
    }

    public void hset(String hkey, String field, String value, long expire) throws DataAccessException {
        if (null == value) {
            value = "";
        }
        hashOperations.put(hkey, field, value);
        if (expire != -1) {
            redisTemplate.expire(hkey, expire, TimeUnit.SECONDS);
        }
    }

    public void hsetForObject(String hkey, Object object, long expire){
        //List<Map<String, String>> list = ObjectUtil.getFiledsInfo(object);
        //if (null != list && list.size() > 0) {
        //    Map<String, Object> keyMap = new HashMap<String, Object>();
        //    for (Map<String, String> map : list) {
        //        if (null != map.get("value")) {
        //            keyMap.put(map.get("name"), String.valueOf(map.get("value")));
        //        }
        //    }
        //    hashOperations.putAll(hkey, keyMap);
        //    if (expire != -1) {
        //        redisTemplate.expire(hkey, expire, TimeUnit.SECONDS);
        //    }
        //}
        redisTemplate.opsForValue().set(hkey, object);
    }

    public T hgetAllForObject(String hkey, Class<T> clazz){

        //Map<String, Object> map = hashOperations.entries(hkey);
        //if (null == map || map.size() <= 0) {
        //    return null;
        //}
        //Object object = clazz.newInstance();
        //List<Map<String, String>> list = ObjectUtil.getFiledsInfo(object);
        //if (null != list && list.size() > 0) {
        //    for (int i = 0; i < list.size(); i++) {
        //        try {
        //            ObjectUtil.setProperty(object, list.get(i).get("name"), map.get(list.get(i).get("name")));
        //        } catch (Exception e) {
        //            e.printStackTrace();
        //        }
        //    }
        //}

        return (T)redisTemplate.opsForValue().get(hkey);
    }

    public void hremove(String hkey, String field) throws DataAccessException {
        hashOperations.delete(hkey, field);
    }

    public void delete(String key) throws DataAccessException {
        redisTemplate.delete(key);
    }

    public Map<String, Object> hentries(String key) throws DataAccessException {
        return hashOperations.entries(key);
    }

    public void setString(String key, String value, long expire) throws DataAccessException {
        logger.info("set redis value:" + value);
        valueOperations.set(key, value, expire, TimeUnit.SECONDS);
    }

    public Object getString(String key) throws DataAccessException {
        Object val = valueOperations.get(key);

        if (val != null) {
            logger.info("get redis value:" + val.toString());
            return val.toString().trim();
        }

        return val;
    }

    public void set(String key, String value, long expire) throws DataAccessException {
        stringRedisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public Object get(String key) throws DataAccessException {
        Object val = stringRedisTemplate.opsForValue().get(key);
        if (val != null) {
            return val.toString().trim();
        }
        return val;
    }
}
