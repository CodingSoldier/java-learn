package com.example.yyghspringbootredisstarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 当
 */
@ConditionalOnProperty(name = "spring.redis.yygh-redis.enable", havingValue = "true")
@Import(YyghRedisConfig.class)
public class YyghRedisAutoConfiguration {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    @ConditionalOnMissingBean(YyghRedisUtil.class)
    public YyghRedisUtil yyghRedisUtil(){
        return new YyghRedisUtil(redisTemplate);
    }
}
