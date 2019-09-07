package com.example.cauth2.social.qq.connect;


import com.example.cauth2.social.qq.QQServiceProvider;
import com.example.cauth2.social.qq.api.QQApi;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * QQ连接工厂
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQApi> {

    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }

}
