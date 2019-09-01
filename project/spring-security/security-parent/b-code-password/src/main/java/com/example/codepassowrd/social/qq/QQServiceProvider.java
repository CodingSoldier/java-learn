package com.example.codepassowrd.social.qq;

import com.example.codepassowrd.social.qq.api.QQApi;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2Template;

/**
 * QQ服务提供者
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQApi> {

    //获取授权码的url地址
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    //获取token令牌的url地址
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    // 连接QQ所需的appId
    private String appId;

    public QQServiceProvider(String appId, String appSecret) {
        // 服务提供商中的OAuth2Operations
        super(new OAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
    }

    @Override
    public QQApi getApi(String accessToken) {
        // 每个用户的accessToken都不同，QQApi要是多实例的
        // 服务提供商中的Api
        return new QQApi(accessToken, appId);
    }
}
