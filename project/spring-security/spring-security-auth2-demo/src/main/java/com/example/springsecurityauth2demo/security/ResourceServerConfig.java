package com.example.springsecurityauth2demo.security;

import com.example.springsecurityauth2demo.security.exceptionhandler.CustomAccessDeniedHandler;
import com.example.springsecurityauth2demo.security.exceptionhandler.CustomAuthExceptionEntryPoint;
import com.example.springsecurityauth2demo.security.noservercheck.CustomResourceServerTokenServices;
import com.example.springsecurityauth2demo.security.noservercheck.CustomUserAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * 资源服务器
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    // 签名秘钥，写到配置文件中
    //@Value("${custom.token.jwtSecret}")
    private String jwtSecret = "jwtSigningKeyValue";

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                //consul、swagger不认证
                .antMatchers("/swagger-ui.html","/webjars/**", "/swagger-resources/**", "/**/v2/api-docs",
                        "/info", "/test/no/token/**")
                .permitAll()
                //其他请求都要认证
                .anyRequest()
                .authenticated()
                .and()
                //关闭跨站请求防护
                .csrf().disable();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
        CustomUserAuthenticationConverter userTokenConverter = new CustomUserAuthenticationConverter();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);

        CustomResourceServerTokenServices tokenServices = new CustomResourceServerTokenServices();

        JwtTokenStore jwtTokenStore = new JwtTokenStore(jwtTokenConverter());
        tokenServices.setTokenStore(jwtTokenStore);
        tokenServices.setJwtAccessTokenConverter(jwtTokenConverter());
        tokenServices.setDefaultAccessTokenConverter(accessTokenConverter);

        resources.accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomAuthExceptionEntryPoint())
                .tokenServices(tokenServices);
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenConverter() {
        JwtAccessTokenConverter jwtTokenConverter = new JwtAccessTokenConverter();
        // 这里的签名key 保持和认证中心一致
        jwtTokenConverter.setSigningKey(jwtSecret);
        return jwtTokenConverter;
    }

}