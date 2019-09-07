package com.example.esecurity.security;

import com.example.esecurity.handler.CustomAuthenticationFailureHandler;
import com.example.esecurity.handler.CustomAuthenticationSuccessHandler;
import com.example.esecurity.imagecode.ImageCodeValidateFilter;
import com.example.esecurity.sms.SmsCodeValidateFilter;
import com.example.esecurity.sms.SmsMobileAuthenticationSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

@EnableResourceServer
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    String[] permitUrl = {"/sign-in.html", "/sign-up.html",
            "/authentication/request", "/code/*", "/qqLogin/**", "/user/**"};

    @Autowired
    CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Autowired
    CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    @Autowired
    SmsMobileAuthenticationSecurityConfig smsMobileAuthenticationSecurityConfig;

    // 图片验证码
    @Autowired
    ImageCodeValidateFilter validateCodeFilter;
    // 短信验证码
    @Autowired
    SmsCodeValidateFilter smsCodeValidateFilter;

    //社交配置
    @Autowired
    SpringSocialConfigurer springSocialConfigurer;


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // 4、添加自定义过滤器filter，图片验证码过滤器、短信验证码过滤器、手机号校验过滤链
        // 在账号密码校验过滤器前面添加validateCodeFilter
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
        //         在账号密码校验过滤器前面添加短信验证码过滤器
                .addFilterBefore(smsCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
        .formLogin()   //表单登陆
                //.loginPage("/authentication/request")  // 请求资源未授权，跳转到此controller接口
                // 用户名密码登陆默认使用UsernamePasswordAuthenticationFilter处理，登陆url是/login
                //.loginProcessingUrl(Constants.URL_LOGIN_PASSWORD)
                // 登录成功后，调用authenticationSuccessHandler，返回response
                .successHandler(customAuthenticationSuccessHandler)
                // 登陆失败处理器
                .failureHandler(customAuthenticationFailureHandler)
                .and()
                // 应用手机号+短信验证码中的手机号校验
                .apply(smsMobileAuthenticationSecurityConfig)
                .and()
                //社交配置
                //.apply(springSocialConfigurer)
                //.and()
                .authorizeRequests()   //允许请求
                //登录页面不需要授权,不配置这个会导致页面跳转死循环
                .antMatchers(permitUrl).permitAll()
                .anyRequest()    //任何请求
                .authenticated()  //需要校验
                .and()
                .cors()
                .and()
                .csrf().disable();  //禁用csrf，否则，登录后会重定向到登录页

    }


}
