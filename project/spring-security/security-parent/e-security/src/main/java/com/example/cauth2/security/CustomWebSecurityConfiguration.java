package com.example.cauth2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * web安全配置适配器
 */
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


//String[] permitUrl = {"/sign-in.html", "/sign-up.html",
    //        "/authentication/request", "/code/*", "/qqLogin/**", "/user/**", "/oauth/**"};
    //
    //@Autowired
    //AuthenticationSuccessHandler authenticationSuccessHandler;
    //@Autowired
    //AuthenticationFailureHandler authenticationFailureHandler;
    //@Autowired
    //SmsMobileAuthenticationSecurityConfig smsMobileAuthenticationSecurityConfig;
    //
    //// 图片验证码
    //@Autowired
    //ImageCodeValidateFilter validateCodeFilter;
    //// 短信验证码
    //@Autowired
    //SmsCodeValidateFilter smsCodeValidateFilter;
    //
    ////社交配置
    //@Autowired
    //SpringSocialConfigurer springSocialConfigurer;
    //
    ///**
    // * http安全配置
    // */
    //@Override
    //protected void configure(HttpSecurity http) throws Exception {
    //
    //    // 4、添加自定义过滤器filter，图片验证码过滤器、短信验证码过滤器、手机号校验过滤链
    //    // 在账号密码校验过滤器前面添加validateCodeFilter
    //    http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
    //            // 在账号密码校验过滤器前面添加短信验证码过滤器
    //            .addFilterBefore(smsCodeValidateFilter, UsernamePasswordAuthenticationFilter.class)
    //            .formLogin()   //表单登陆
    //            .loginPage("/authentication/request")  // 请求资源未授权，跳转到此controller接口
    //            // 用户名密码登陆默认使用UsernamePasswordAuthenticationFilter处理，登陆url是/login
    //            .loginProcessingUrl(Constants.URL_LOGIN_PASSWORD)
    //            // 登录成功后，调用authenticationSuccessHandler，返回response
    //            .successHandler(authenticationSuccessHandler)
    //            // 登陆失败处理器
    //            .failureHandler(authenticationFailureHandler)
    //            .and()
    //            // 应用手机号+短信验证码中的手机号校验
    //            .apply(smsMobileAuthenticationSecurityConfig)
    //            .and()
    //            //社交配置
    //            .apply(springSocialConfigurer)
    //            .and()
    //            .authorizeRequests()   //允许请求
    //            //登录页面不需要授权,不配置这个会导致页面跳转死循环
    //            .antMatchers(permitUrl).permitAll()
    //            .anyRequest()    //任何请求
    //            .authenticated()  //需要校验
    //            .and()
    //            .csrf().disable();  //禁用csrf，否则，登录后会重定向到登录页
    //
    //
    //
    //    //// 3、需要校验的url跳转到一个controller接口
    //    //http.formLogin()   //表单登陆
    //    //        .loginPage("/authentication/request")  // 请求资源未授权，跳转到此controller接口
    //    //        //用户名密码登陆默认使用UsernamePasswordAuthenticationFilter处理，登陆url是/login
    //    //        .loginProcessingUrl(Constants.URL_LOGIN_PASSWORD)
    //    //        // 登录成功后，调用authenticationSuccessHandler，返回response
    //    //        .successHandler(authenticationSuccessHandler)
    //    //        // 登陆失败处理器
    //    //        .failureHandler(authenticationFailureHandler)
    //    //        .and()
    //    //        .authorizeRequests()   //允许请求
    //    //        //登录页面不需要授权,不配置这个会导致页面跳转死循环
    //    //        .antMatchers("/sign-in.html", "/authentication/request", "/code/image").permitAll()
    //    //        .anyRequest()    //任何请求
    //    //        .authenticated()  //需要校验
    //    //        .and()
    //    //        .csrf().disable();  //禁用csrf，否则，登录后会重定向到登录页
    //
    //
    //
    //    //// 2、添加登陆页面
    //    //http.formLogin()   //表单登陆
    //    //        .loginPage("/sign-in.html")  // 添加登陆页面
    //    //        //用户名密码登陆默认使用UsernamePasswordAuthenticationFilter处理，登陆url是/login
    //    //        .loginProcessingUrl("/login/password")
    //    //        .and()
    //    //        .authorizeRequests()   //允许请求
    //    //        //登录页面不需要授权,不配置这个会导致页面跳转死循环
    //    //        .antMatchers("/sign-in.html").permitAll()
    //    //        .anyRequest()    //任何请求
    //    //        .authenticated()  //需要校验
    //    //        .and()
    //    //        .csrf().disable();  //禁用csrf，否则，登录后会重定向到登录页
    //
    //
    //
    //    // 1、未添加登陆页面
    //    //http.formLogin()   //表单登陆
    //    ////http.httpBasic()   // 弹窗形式的登陆框，但貌似都表单页面登陆
    //    //        .and()
    //    //        .authorizeRequests()   //允许请求
    //    //        .anyRequest()    //任何请求
    //    //        .authenticated();    //需要校验
    //
    //
    //}
}
