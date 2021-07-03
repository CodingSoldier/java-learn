package com.example.yyghspringbootredisstarter;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 * @param <K>
 * @param <V>
 */
public class YyghRedisUtil<K, V> {

    private static RedisTemplate redisTemplate;

    private static ValueOperations valueOps;

    public YyghRedisUtil(RedisTemplate<K, V> redisTemplate) {
        YyghRedisUtil.redisTemplate = redisTemplate;
        YyghRedisUtil.valueOps = redisTemplate.opsForValue();
    }

    /**
     * 获取RedisTemplate
     * @return
     */
    public static RedisTemplate getRedisTemplate(){
       return redisTemplate;
    }

    /**
     * key是否存在
     * @param key
     * @return
     */
    public static Boolean hasKey(Object key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置值
     * @param key 键
     * @param value 值
     * @param timeout 过期时间
     * @param unit 时间单位
     */
    public static void set(Object key, Object value, long timeout, TimeUnit unit){
        valueOps.set(key, value, timeout, unit);
    }

    /**
     * 设置值
     * @param key 键
     * @param value 值
     */
    public static void set(Object key, Object value){
        valueOps.set(key, value);
    }

    /**
     * 获取值
     * @param key 键
     * @param type 返回类型
     * @param <V>
     * @return
     */
    public static <V> V get(Object key, Class<V> type){
        Object value = valueOps.get(key);
        return (V)value;
    }


}
