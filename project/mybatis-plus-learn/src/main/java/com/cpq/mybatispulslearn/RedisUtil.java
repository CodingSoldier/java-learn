package com.cpq.mybatispulslearn;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * 缓存工具类
 *
 * @param <T>
 */
@Slf4j
@Component
public class RedisUtil<T> {

    @Autowired
    private RedisTemplate redisTemplate;

    protected HashOperations hashOperations;
    protected SetOperations setOperations;
    protected ValueOperations valueOperations;
    protected ZSetOperations zSetOperations;

    // 默认失效时间
    long redisExpire = -1;

    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.opsForValue();
        hashOperations = redisTemplate.opsForHash();
        setOperations = redisTemplate.opsForSet();
        zSetOperations = redisTemplate.opsForZSet();
    }

    /*template*/
    //通过key删除缓存
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /*zSet*/
    public Long addZSet(String key, Set<ZSetOperations.TypedTuple> set){
        return zSetOperations.add(key, set);
    }

}