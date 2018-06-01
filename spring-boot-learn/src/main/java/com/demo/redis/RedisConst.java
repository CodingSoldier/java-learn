package com.demo.redis;

import org.springframework.stereotype.Component;

@Component("redisConst")
public class RedisConst {
    public static final String SPRING_CACHE = "SPRING:CACHE:";
    public static final String SPRING_CACHE_ID = "213142sfgdgbdfg";

    public String getId(){
        return SPRING_CACHE_ID;
    }
}
