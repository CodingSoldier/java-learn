package com.example.codepassword.social.qq.connect;

import com.example.codepassword.social.qq.api.QQApi;
import com.example.codepassword.social.qq.api.QQUserInfo;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * QQ连接适配器
 */
public class QQAdapter implements ApiAdapter<QQApi> {

    // 判断能否连接QQ
    @Override
    public boolean test(QQApi api) {
        return true;
    }

    /**
     * 将服务提供商提供的用户信息设置到标准用户信息上
     */
    @Override
    public void setConnectionValues(QQApi api, ConnectionValues values) {
        QQUserInfo qqUserInfo = api.getUserInfo();
        // 用户名
        values.setDisplayName(qqUserInfo.getNickname());
        // 头像url
        values.setImageUrl(qqUserInfo.getFigureurl_1());
        // 个人主页
        values.setProfileUrl(null);
        // 服务商的id，opendid
        values.setProviderUserId(qqUserInfo.getOpenId());
    }

    // 用户主页，QQ无，微博有
    @Override
    public UserProfile fetchUserProfile(QQApi api) {
        return null;
    }

    // 状态，QQ无，微博有(比如发微博)
    @Override
    public void updateStatus(QQApi api, String message) {

    }
}
