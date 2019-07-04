//package com.cpq.redis;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
//import java.util.List;
//
///**
// JedisClusterConfig 配置
// */
//@Configuration
//public class RedisConfigLettuce {
//
//    /**
//        ## cluster集群模式，使用lettuce客户端没法做故障迁移
//        ## 连接池最大连接数（使用负值表示没有限制）
//        #spring.redis.lettuce.pool.max-active=8
//        ## 连接池中的最大空闲连接
//        #spring.redis.lettuce.pool.max-idle=8
//        ## 连接池中的最小空闲连接
//        #spring.redis.lettuce.pool.min-idle=0
//        ## 连接池最大阻塞等待时间（使用负值表示没有限制）
//        #spring.redis.lettuce.pool.max-wait=1000
//        ## 关闭超时时间
//        #spring.redis.lettuce.shutdown-timeout=100
//        #
//        #spring.redis.cluster.nodes=192.168.4.176:7000, 192.168.4.176:7001, 192.168.4.176:7002, 192.168.4.176:7003, 192.168.4.176:7004, 192.168.4.176:7005
//        ### 重定向次数
//        #spring.redis.cluster.max-redirects=5
//
//      */
//
//    @Value("${spring.redis.lettuce.pool.max-active}")
//    private Integer poolMaxActive;
//    @Value("${spring.redis.lettuce.pool.max-idle}")
//    private Integer poolMaxIdle;
//    @Value("${spring.redis.lettuce.pool.min-idle}")
//    private Integer poolMinIdle;
//    @Value("${spring.redis.lettuce.pool.max-wait}")
//    private Integer poolMaxWait;
//    @Value("${spring.redis.cluster.nodes}")
//    private List<String> clusterNodes;
//    @Value("${spring.redis.cluster.max-redirects}")
//    private Integer clusterMaxRedirects;
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
//        poolConfig.setMaxTotal(poolMaxActive);
//        poolConfig.setMaxIdle(poolMaxIdle);
//        poolConfig.setMinIdle(poolMinIdle);
//        poolConfig.setMaxWaitMillis(poolMaxWait);
//        LettuceClientConfiguration clientConfig =
//                LettucePoolingClientConfiguration.builder().commandTimeout(Duration.ofMillis(100))
//                        .poolConfig(poolConfig).build();
//
//        // 集群redis
//        RedisClusterConfiguration redisConfig = new RedisClusterConfiguration();
//        redisConfig.setMaxRedirects(clusterMaxRedirects);
//        for (String ipPort :clusterNodes){
//            String[] ipPortArr = ipPort.split(":");
//            redisConfig.clusterNode(ipPortArr[0], Integer.parseInt(ipPortArr[1]));
//        }
//
//        return new LettuceConnectionFactory(redisConfig, clientConfig);
//    }
//
//
//    @Bean
//    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate template = new RedisTemplate();
//        template.setConnectionFactory(redisConnectionFactory);
//
//        //key使用StringRedisSerializer
//        StringRedisSerializer strSerializer = new StringRedisSerializer();
//        template.setKeySerializer(strSerializer);
//        template.setHashKeySerializer(strSerializer);
//
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        //value使用Jackson2JsonRedisSerializer
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//        return template;
//    }
//
//
//}