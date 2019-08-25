package com.example.sso1client.sso.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class OAuth2Configurer extends ResourceServerConfigurerAdapter {
    @Bean
    public ResourceServerTokenServices tokenServices() {
        // 与授权服务器保持一致
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(getTokenStore());
        tokenServices.setTokenEnhancer(tokenConverter());

        return tokenServices;
    }

    public TokenStore getTokenStore() {
        return new JwtTokenStore(tokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        // 与服务器端保持一致，注意这里需要将TokenConverter注入到容器中去，
        // 否则会报错：InvalidTokenException, Cannot convert access token to JSON
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("7Ub2aV");
        return converter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        // 配置访问的资源
        resources.tokenServices(tokenServices());
        super.configure(resources);
    }
}
