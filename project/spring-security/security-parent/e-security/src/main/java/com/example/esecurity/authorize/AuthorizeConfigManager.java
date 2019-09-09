package com.example.esecurity.authorize;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class AuthorizeConfigManager {

    @Autowired
    private Set<AuthorizeConfigProvider> authorizeConfigProviderSet;

    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config){
        for (AuthorizeConfigProvider authorizeConfigProvider:authorizeConfigProviderSet){
            authorizeConfigProvider.config(config);
        }
        config.anyRequest().authenticated();
    }

}
