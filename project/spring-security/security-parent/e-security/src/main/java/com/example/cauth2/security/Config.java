package com.example.cauth2.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Config {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenStore redisTokenStore(){
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("c-auth2");
        return redisTokenStore;
    }

    // jwt转换器
    @Bean
    protected JwtAccessTokenConverter jwtTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("jwtSigningKey");
        return converter;
    }

    /**
     * token增强器，
     * 使用@bean实现TokenEnhancer这个接口
     */
    @Bean
    public TokenEnhancer tokenEnhancer(){
        return (accessToken, authorization) -> {
            Map<String, Object> addInfo = new HashMap<>();
            addInfo.put("add_info_key", "add_info_value");
            ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(addInfo);
            return accessToken;
        };
    }



}


























