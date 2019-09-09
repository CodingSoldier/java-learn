package com.example.esecurity.authorize;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthorizeConfigProvider implements AuthorizeConfigProvider {

    String[] permitUrl = {"/sign-in.html", "/sign-up.html",
            "/authentication/request", "/code/**", "/qqLogin/**"};

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(permitUrl).permitAll()
        .antMatchers(HttpMethod.GET, "/security/role/admin").hasRole("ADMIN")
        .antMatchers(HttpMethod.GET, "/security/role/user").hasRole("USER");
    }
}
