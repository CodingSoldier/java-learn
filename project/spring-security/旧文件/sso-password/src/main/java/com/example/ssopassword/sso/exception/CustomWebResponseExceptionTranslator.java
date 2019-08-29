package com.example.ssopassword.sso.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

import java.util.HashMap;
import java.util.Map;

//这是获取token阶段出现异常部分
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity translate(Exception e) {
        Map<String, Object> result = new HashMap<>();
        result.put("resultCode", "401");
        if (e instanceof InternalAuthenticationServiceException){
            result.put("resultMsg", "用户不存在");
        }else if (e instanceof InvalidGrantException) {
            result.put("resultMsg", "密码错误");
        } else if (e instanceof InvalidTokenException) {
            result.put("resultMsg", "token未识别");
        }else {
            result.put("resultMsg", e.getMessage());
        }
        return ResponseEntity.ok(result);
    }
}
