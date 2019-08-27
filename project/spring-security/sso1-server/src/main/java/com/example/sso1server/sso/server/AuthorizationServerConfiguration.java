package com.example.sso1server.sso.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;

/**
 * 认证服务器
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private PasswordEncoder bCryptPasswordEncoder;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private TokenStore redisTokenStore;
    @Resource
    private JwtAccessTokenConverter jwtTokenConverter;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 配置客户端信息
        clients.inMemory()
                .withClient("test")
                .secret(bCryptPasswordEncoder.encode("secret"))
                .authorizedGrantTypes("password")
                .scopes("all");
    }

    @Bean
    public WebResponseExceptionTranslator loggingExceptionTranslator() {
        return new DefaultWebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
                // This is the line that prints the stack trace to the log. You can customise this to format the trace etc if you like
                e.printStackTrace();

                // Carry on handling the exception
                ResponseEntity<OAuth2Exception> responseEntity = super.translate(e);
                HttpHeaders headers = new HttpHeaders();
                headers.setAll(responseEntity.getHeaders().toSingleValueMap());
                OAuth2Exception excBody = responseEntity.getBody();
                return new ResponseEntity<>(excBody, headers, responseEntity.getStatusCode());
            }
        };
    }
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(redisTokenStore)
                .accessTokenConverter(jwtTokenConverter)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false)
                .authenticationManager(authenticationManager);
        endpoints.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);// add get method
        endpoints.exceptionTranslator(loggingExceptionTranslator());
        //endpoints.exceptionTranslator(webResponseExceptionTranslator());//认证异常翻译
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }



}