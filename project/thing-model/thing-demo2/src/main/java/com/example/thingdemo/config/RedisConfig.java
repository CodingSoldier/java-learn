package com.example.thingdemo.config;

import com.example.thingdemo.util.ObjectMapperUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = ObjectMapperUtil.newObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(om);

        //key使用StringRedisSerializer
        StringRedisSerializer strSerializer = new StringRedisSerializer();
        template.setKeySerializer(strSerializer);
        //value使用Jackson2JsonRedisSerializer
        template.setValueSerializer(jackson2JsonRedisSerializer);

        template.setHashKeySerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        return template;
    }

}