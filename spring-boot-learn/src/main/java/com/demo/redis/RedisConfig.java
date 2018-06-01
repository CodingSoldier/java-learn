package com.demo.redis;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    //@Value("${spring.redis.database}")
    //private int database;
    //
    //@Value("${spring.redis.host}")
    //private String host;
    //
    //@Value("${spring.redis.port}")
    //private int port;
    //
    //@Value("${spring.redis.timeout}")
    //private int timeout;
    //
    //@Value("${spring.redis.pool.max-idle}")
    //private int maxidle;
    //
    //@Value("${spring.redis.pool.min-idle}")
    //private int minidle;
    //
    //@Value("${spring.redis.pool.max-active}")
    //private int maxActive;
    //
    //@Value("${spring.redis.pool.max-wait}")
    //private long maxWait;

    //默认key生成策略为类全名+方法名+参数
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            //StringBuffer是线程安全的
            StringBuffer sb = new StringBuffer();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }
    //
    //@Bean
    //public KeyGenerator keyGeneratorCustom() {
    //    return (target, method, params) -> {
    //        //StringBuffer是线程安全的
    //        StringBuffer sb = new StringBuffer();
    //        sb.append(target.getClass().getName());
    //        sb.append(method.getName());
    //        for (Object obj : params) {
    //            sb.append(obj.toString());
    //        }
    //        return sb.toString();
    //    };
    //}

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        //单位是秒
        cacheManager.setDefaultExpiration(60*60*24*30);
        return cacheManager;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(factory);

        //key使用StringRedisSerializer
        StringRedisSerializer strSerializer = new StringRedisSerializer();
        template.setKeySerializer(strSerializer);
        template.setHashKeySerializer(strSerializer);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        //value使用Jackson2JsonRedisSerializer
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);

        return template;
    }


}