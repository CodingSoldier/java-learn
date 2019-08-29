package com.example.sso1server.sso.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-26
 */
@Configuration
public class Config {

    //@Resource
    //private RedisConnectionFactory lettuceConnectionFactory;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    public TokenStore getTokenStore() {
        return new JwtTokenStore(tokenConverter());
    }
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        // 配置Token加密Key，注意要与客户端保持一致; 也可以配置成RSA方式；
        converter.setSigningKey("7Ub2aV");
        return converter;
    }
    @Bean
    public DefaultTokenServices tokenServices() {
        // 配置TokenServices，实际项目中可以使用其它的JdbcClientTokenServices等进行持久化
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(getTokenStore());      // 配置Token存储方式
        tokenServices.setTokenEnhancer(tokenConverter());  // 配置Token转换器

        return tokenServices;
    }

    //@Override
    //public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
    //    endpoints.authenticationManager(authenticationManager)
    //            .tokenServices(tokenServices());
    //}

    //@Bean
    //public TokenStore redisTokenStore() {
    //    return new RedisTokenStore(redisConnectionFactory());
    //}

    //public RedisConnectionFactory redisConnectionFactory() {
    //    return new JedisConnectionFactory();
    //}
    @Bean
    public TokenStore redisTokenStore() {
        RedisTokenStore redis = new RedisTokenStore(new LettuceConnectionFactory());
        // 缓存前缀
        redis.setPrefix("token:poly:auth-center:");
        return redis;
    }

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtTokenConverter());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                //SysUser user = (SysUser) authentication.getUserAuthentication().getPrincipal();
                //Map<String, Object> infoMap = new HashMap<>();
                //infoMap.put("userId", user.getId());
                //infoMap.put("userType", user.getType());//用户类型 @see UserTypeEnum
                //infoMap.put("userName", user.getUsername());//用户名
                //infoMap.put("loginType", user.getLoginType());//登录类型 @see LoginTypeEnum
                //infoMap.put("superAdmin", user.isSuperAdmin());
                //infoMap.put("employeeIds", user.getEmployeeIds());
//                infoMap.put("roles", user.getAuthorities());
//                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(infoMap);
                return super.enhance(accessToken, authentication);
            }
        };
        converter.setSigningKey("123123234");
        return converter;
    }



}
