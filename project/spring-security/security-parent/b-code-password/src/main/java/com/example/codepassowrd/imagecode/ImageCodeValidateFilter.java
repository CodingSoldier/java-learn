package com.example.codepassowrd.imagecode;

import com.example.codepassowrd.Constants;
import com.example.codepassowrd.exception.CustomAuthenticationException;
import com.example.codepassowrd.handler.AuthenticationFailureHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义过滤器，验证码过滤器
 * OncePerRequestFilter，每次请求只调用一次
 */
@Component
public class ImageCodeValidateFilter extends OncePerRequestFilter {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 账号密码登陆，校验图片验证码
        if (Constants.URL_LOGIN_PASSWORD.equals(request.getRequestURI())){
            try {
                validateImageCode(new ServletWebRequest(request));
            }catch (Exception e){
                // 校验图片验证码抛出异常，直接调用失败handler
                authenticationFailureHandler.onAuthenticationFailure(request, response, new CustomAuthenticationException(e.getMessage()));
                // 返回，不再调用后续链路
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    // 验证图片验证码
    public void validateImageCode(ServletWebRequest request) throws ServletRequestBindingException {
        // 存储在session中的图片验证码
        ImageCode imageCode = (ImageCode)sessionStrategy.getAttribute(request, ImageCodeController.SESSION_KEY_IMAGE_CODE);
        // 前台传过来的图片验证码
        String codeParam = ServletRequestUtils.getStringParameter(request.getRequest(), Constants.IMGAE_CODE);
        if (!StringUtils.equals(imageCode.getCode(), codeParam)){
            throw new RuntimeException("验证码不匹配");
        }
        //sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY_IMAGE_CODE);
    }
}
