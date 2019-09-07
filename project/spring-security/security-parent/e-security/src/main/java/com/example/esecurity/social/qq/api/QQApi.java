package com.example.esecurity.social.qq.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

/**
 * QQ接口工具类
 * 此类包含了属性accessToken，每个账号的AccessToken都不一样，所以本类是多实例的，不能是单例
 */
@Slf4j
public class QQApi extends AbstractOAuth2ApiBinding {

    // 获取openid
    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";
    //获取用户信息，spring-social会自动拼接access_token
    private static final String URL_GET_USER_INFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appId;
    private String openId;

    public QQApi(String accessToken, String appId) {
        // accessToken放在请求参数中
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;

        //获取openId
        String openIdUrl = String.format(URL_GET_OPENID, accessToken);
        String result = getRestTemplate().getForObject(openIdUrl, String.class);

        log.info(result);

        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");
    }

    public QQUserInfo getUserInfo(){
        String userInfoUrl = String.format(URL_GET_USER_INFO, appId, openId);
        String result = getRestTemplate().getForObject(userInfoUrl, String.class);
        log.info(result);

        QQUserInfo qqUserInfo = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            qqUserInfo = objectMapper.readValue(result, QQUserInfo.class);
            qqUserInfo.setOpenId(openId);
        }catch (Exception e){
            log.error("获取QQ用户信息失败", e);
        }
        return qqUserInfo;
    }
}
