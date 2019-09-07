package com.example.esecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;

/**
 * 认证服务器
 * @Configuration 这个注解不能少，少了这个注解，类名会变灰色
 */
@Configuration
@EnableAuthorizationServer
public class CustomAuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Value("${custom.security.oauth2.client.client-id}")
    private String clientId;
    @Value("${custom.security.oauth2.client.client-secret}")
    private String clientSecret;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    @Lazy
    AuthenticationManager authenticationManagerBean;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    TokenStore redisTokenStore;
    @Autowired
    JwtAccessTokenConverter jwtTokenConverter;
    @Autowired
    TokenEnhancer tokenEnhancer;

    /**
     * 客户端配置
     * 配置客户端id、密码、授权范围、认证类型
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(clientSecret))
                .accessTokenValiditySeconds(7200)
                .authorizedGrantTypes("authorization_code", "password", "refresh_token")
                .scopes("all", "read", "write");  //客户端发请求带的scope要是这里面的几个

        /**
         * 配置多个客户端，.and().withClient(clientID)
         */

    }

    /**
     * 端点配置，服务端处理
     * 解决 Unsupported grant type: password
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 使用TokenEnhanceChain把token转换器、token增强器连接起来
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenConverter, tokenEnhancer));

        endpoints.authenticationManager(authenticationManagerBean)
            .userDetailsService(customUserDetailsService)
            .tokenStore(redisTokenStore)
            .tokenEnhancer(tokenEnhancerChain);
    }

    // 解决 oauth/token  401
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.tokenKeyAccess("permitAll()")   //访问token key(jwt的签名)，允许。默认是denyAll()
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

}
