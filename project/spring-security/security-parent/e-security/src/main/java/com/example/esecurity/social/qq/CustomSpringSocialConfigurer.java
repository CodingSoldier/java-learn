package com.example.esecurity.social.qq;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

/**
 * 重写SpringSocialConfigurer，修改请求url前缀
 */
public class CustomSpringSocialConfigurer  extends SpringSocialConfigurer{

    private String filterProcessesUrl;

    public CustomSpringSocialConfigurer(String filterProcessesUrl) {
        this.filterProcessesUrl = filterProcessesUrl;
    }

    //修改qq请求url前缀
    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter)super.postProcess(object);
        filter.setFilterProcessesUrl(filterProcessesUrl);
        return (T)filter;
    }
}
