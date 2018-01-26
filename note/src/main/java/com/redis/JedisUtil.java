package com.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018/1/26
 */
public class JedisUtil {

    private static final JedisPool POOL;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(50);
        config.setMaxIdle(10);
        POOL = new JedisPool(config, "192.168.40.129", 6379);
    }

    public static Jedis getJedis(){
        return POOL.getResource();
    }

}
