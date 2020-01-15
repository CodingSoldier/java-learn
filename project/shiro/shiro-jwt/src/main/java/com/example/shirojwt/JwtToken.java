package com.example.shirojwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author chenpiqian
 * @date: 2020-01-15
 */
public class JwtToken implements AuthenticationToken {

    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    // token作为principal
    @Override
    public Object getPrincipal() {
        return this.token;
    }

    // credentials设置为空字符串
    @Override
    public Object getCredentials() {
        return "";
    }
}
