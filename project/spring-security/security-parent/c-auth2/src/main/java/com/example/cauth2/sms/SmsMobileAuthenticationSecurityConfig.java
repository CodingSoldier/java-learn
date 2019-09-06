package com.example.cauth2.sms;

import com.example.cauth2.handler.CustomAuthenticationFailureHandler;
import com.example.cauth2.handler.CustomAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 整合手机+验证码登陆 中的 手机号验证 的整条链路
 */
@Component
public class SmsMobileAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    private UserDetailsService customUserDetailsService;

    @Override
    public void configure(HttpSecurity http) {
        SmsMobileAuthenticationFilter smsMobileAuthenticationFilter = new SmsMobileAuthenticationFilter();
        // filter链路设置authenticationManager
        smsMobileAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        // 设置成功失败处理器
        smsMobileAuthenticationFilter.setAuthenticationSuccessHandler(customAuthenticationSuccessHandler);
        smsMobileAuthenticationFilter.setAuthenticationFailureHandler(customAuthenticationFailureHandler);

        // Provider注入，UserDetailsService
        SmsMobileAuthenticationProvider smsMobileAuthenticationProvider = new SmsMobileAuthenticationProvider();
        smsMobileAuthenticationProvider.setUserDetailsService(customUserDetailsService);

        // 添加Provider
        // 在UsernamePasswordAuthenticationFilter后添加短信手机过滤器
        http.authenticationProvider(smsMobileAuthenticationProvider)
                .addFilterAfter(smsMobileAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}























