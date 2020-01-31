package com.example.shirojwt.common;

import lombok.Data;
import org.apache.shiro.authc.AuthenticationException;

/**
 * org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException;
 * 此接口抛出AuthenticationException，为了能更好的返回异常信息给前端，新建一个CustomAuthenticationException继承AuthenticationException
 *
 * com.example.shirojwt.JwtRealm#doGetAuthenticationInfo() 抛出 CustomAuthenticationException
 * CustomAuthenticationException比父类多了一个code属性，前端通过code来判断token是否失效
 *
 * 最后通过全局异常处理器com.example.shirojwt.common.MyControllerAdvice#errorHandler()返回自定义信息给前端
 */
@Data
public class CustomAuthenticationException extends AuthenticationException {

    private Integer code = 0;

    public CustomAuthenticationException(String message) {
        super(message);
    }

    public CustomAuthenticationException(Integer code, String message) {
        super(message);
        this.code = code;
    }

}
