package com.example.ssopasswordclient2.sso.client;

import com.example.ssopasswordclient2.sso.exception.CustomAccessDeniedHandler;
import com.example.ssopasswordclient2.sso.exception.MyAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Autowired
    private TokenStore tokenStore;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore)
                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler());
    }
}
