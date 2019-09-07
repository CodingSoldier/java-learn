package com.example.esecurity.sms;

import com.example.esecurity.common.Constants;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 仿照 UsernamePasswordAuthenticationFilter
 */
public class SmsMobileAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public static final String LOGIN_SMS_PARAM_MOBILE = "mobile";

    // filter拦截手机号+短信登陆的url -> Constants.URL_LOGIN_SMS
    public SmsMobileAuthenticationFilter() {
        super(new AntPathRequestMatcher(Constants.URL_LOGIN_SMS, "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("只支持post请求" + request.getMethod());
        }

        // 获取手机号
        String mobile = request.getParameter(LOGIN_SMS_PARAM_MOBILE);
        mobile = mobile != null ? mobile : "";

        // 创建SmsCodeAuthenticationToken
        // 手机号设置到为principal属性
        SmsMobileAuthenticationToken authRequest = new SmsMobileAuthenticationToken(mobile);
        this.setDetails(request, authRequest);

        // AuthenticationManager校验SmsCodeAuthenticationToken
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected void setDetails(HttpServletRequest request, SmsMobileAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

}
