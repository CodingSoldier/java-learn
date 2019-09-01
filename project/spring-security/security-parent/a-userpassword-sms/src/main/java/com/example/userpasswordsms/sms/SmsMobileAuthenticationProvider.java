package com.example.userpasswordsms.sms;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsMobileAuthenticationProvider implements AuthenticationProvider {

    UserDetailsService userDetailsService;

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * 认证手机号是否存在的方法
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 从Authentication中获取principal
        SmsMobileAuthenticationToken authenticationToken = (SmsMobileAuthenticationToken) authentication;
        String principal = (String) authenticationToken.getPrincipal();

        //去userDetailsService.loadUserByUsername中校验用户
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        if (userDetails == null){
            throw new InternalAuthenticationServiceException("手机号未注册");
        }

        //创建一个认证通过后的SmsCodeAuthenticationToken
        SmsMobileAuthenticationToken authenticationResult = new SmsMobileAuthenticationToken(principal, userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        //返回认证后的Authentication
        return authenticationResult;
    }

    /**
     * 当AuthenticationManager中管理的authentication是SmsCodeAuthenticationToken，就启用本类中的authenticate()
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsMobileAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
