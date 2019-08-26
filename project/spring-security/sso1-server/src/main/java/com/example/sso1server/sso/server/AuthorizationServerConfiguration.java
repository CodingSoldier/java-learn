package com.example.sso1server.sso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证服务器
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端信息
        clients.inMemory()
                .withClient("test")
                .secret(passwordEncoder.encode("secret"))
                .authorizedGrantTypes("password")
                .scopes("all");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(redisTokenStore())
                .accessTokenConverter(jwtTokenConverter())
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                .authenticationManager(authenticationManager);
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);// add get method
        //endpoints.exceptionTranslator(webResponseExceptionTranslator());//认证异常翻译
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


    @Bean
    public TokenStore redisTokenStore() {
        RedisTokenStore redis = new RedisTokenStore(connectionFactory);
        // 缓存前缀
        redis.setPrefix("token:poly:auth-center:");
        return redis;
    }

    /*@Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtTokenConverter());
    }*/

    @Bean
    protected JwtAccessTokenConverter jwtTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                SysUser user = (SysUser) authentication.getUserAuthentication().getPrincipal();
                Map<String, Object> infoMap = new HashMap<>();
                //infoMap.put("userId", user.getId());
                //infoMap.put("userType", user.getType());//用户类型 @see UserTypeEnum
                infoMap.put("userName", user.getUsername());//用户名
                //infoMap.put("loginType", user.getLoginType());//登录类型 @see LoginTypeEnum
                //infoMap.put("superAdmin", user.isSuperAdmin());
                //infoMap.put("employeeIds", user.getEmployeeIds());
//                infoMap.put("roles", user.getAuthorities());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(infoMap);
                return super.enhance(accessToken, authentication);
            }
        };
        converter.setSigningKey("123123234");
        return converter;
    }



    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
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

}