package com.example.ssopassword.sso;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import java.util.Date;

public class CustomRedisToken extends RedisTokenStore {
    private ClientDetailsService clientDetailsService;

    public CustomRedisToken(RedisConnectionFactory connectionFactory, ClientDetailsService clientDetailsService) {
        super(connectionFactory);
        this.clientDetailsService = clientDetailsService;
    }

    //为什么需要刷新token的时间，比如默认1个小时，客户一直在操作，到了1个小时，让其登录，这种体验很差，应该是客户啥时候不请求服务器了，隔多长时间
    //认为其token失效
    // 其实这块可以看下源码，在客户端请求过来的时候，首先到达的是org.springframework.security.oauth2.provider.authentication.
    // OAuth2AuthenticationProcessingFilter。然后在请求校验完token有效之后，以当前时间刷新token，具体时间配置在数据库中~~~
    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        OAuth2Authentication result = readAuthentication(token.getValue());
        if (result != null) {
            // 如果token没有失效  更新AccessToken过期时间
            DefaultOAuth2AccessToken oAuth2AccessToken = (DefaultOAuth2AccessToken) token;

            //重新设置过期时间
            int validitySeconds = getAccessTokenValiditySeconds(result.getOAuth2Request());
            if (validitySeconds > 0) {
                oAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + (validitySeconds * 1000L)));
            }

            //将重新设置过的过期时间重新存入redis, 此时会覆盖redis中原本的过期时间
            storeAccessToken(token, result);
        }
        return result;
    }

    protected int getAccessTokenValiditySeconds(OAuth2Request clientAuth) {
        if (clientDetailsService != null) {
            ClientDetails client = clientDetailsService.loadClientByClientId(clientAuth.getClientId());
            Integer validity = client.getAccessTokenValiditySeconds();
            if (validity != null) {
                return validity;
            }
        }
        // default 12 hours.
        int accessTokenValiditySeconds = 60 * 60 * 12;
        return accessTokenValiditySeconds;
    }
}
