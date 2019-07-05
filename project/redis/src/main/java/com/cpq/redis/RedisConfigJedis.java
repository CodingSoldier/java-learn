package com.cpq.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.List;

/**
 JedisClusterConfig 配置
 */
@Configuration
public class RedisConfigJedis{

    /**

     ## cluster集群模式，使用jedis客户端能故障迁移。而且jedis版本必须是2.XX版本
     spring.redis.timeout=2000
     spring.redis.jedis.pool.max-active=8
     spring.redis.jedis.pool.max-idle=8
     spring.redis.jedis.pool.min-idle=0
     spring.redis.jedis.pool.max-wait=1000

     spring.redis.cluster.nodes=192.168.4.176:7000, 192.168.4.176:7001, 192.168.4.176:7002, 192.168.4.176:7003, 192.168.4.176:7004, 192.168.4.176:7005
     ## 重定向次数
     spring.redis.cluster.max-redirects=5

      */

    @Value("${spring.redis.timeout}")
    private Integer redisTimeout;
    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer poolMaxActive;
    @Value("${spring.redis.jedis.pool.max-idle}")
    private Integer poolMaxIdle;
    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer poolMinIdle;
    @Value("${spring.redis.jedis.pool.max-wait}")
    private Integer poolMaxWait;
    @Value("${spring.redis.cluster.nodes}")
    private List<String> clusterNodes;
    @Value("${spring.redis.cluster.max-redirects}")
    private Integer clusterMaxRedirects;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(poolMaxActive);
        poolConfig.setMaxIdle(poolMaxIdle);
        poolConfig.setMinIdle(poolMinIdle);
        poolConfig.setMaxWaitMillis(poolMaxWait);
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .usePooling().poolConfig(poolConfig).and().readTimeout(Duration.ofMillis(redisTimeout)).build();

        // 集群redis
        RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
        redisConfig.setMaxRedirects(clusterMaxRedirects);
        for (String ipPort :clusterNodes){
            String[] ipPortArr = ipPort.split(":");
            redisConfig.clusterNode(ipPortArr[0], Integer.parseInt(ipPortArr[1]));
        }

        return new JedisConnectionFactory(redisConfig, clientConfig);
    }


    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

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