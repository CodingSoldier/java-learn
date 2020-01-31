package com.example.shirojwt;

import com.example.shirojwt.common.Constant;
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

    // token作为principal，
    @Override
    public Object getPrincipal() {
        return this.token;
    }

    // 由于getPrincipal没返回username，而是返回token，所以credentials设置为空字符串即可
    @Override
    public Object getCredentials() {
        return Constant.CREDENTIALS_EMPTY;
    }
}
