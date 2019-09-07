package com.example.esecurity.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

/**
 * 配置ConnectionSignUp实现类
 * 实现用户使用第三方登录默认注册一个用户并登录
 */
@Component
public class CustomConnectionSignUp implements ConnectionSignUp {

    @Override
    public String execute(Connection<?> connection) {
        return connection.getDisplayName();
    }

}
