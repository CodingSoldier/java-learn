package com.example.cauth2.controller;

import com.example.cauth2.social.SocialUserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserCtrl {

    @Autowired
    ProviderSignInUtils providerSignInUtils;

    @GetMapping("/user")
    public Object test(){
        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);
        result.put("message", "访问成功");
        result.put("user", SecurityContextHolder.getContext().getAuthentication());
        return result;
    }

    // 获取社交用户信息，QQ页面跳转后，在前端调用这个页面，获取信息
    @GetMapping("/user/social")
    public SocialUserInfo getSocialUserInfo(HttpServletRequest request){
        SocialUserInfo userInfo = new SocialUserInfo();
        Connection<?> connection = providerSignInUtils
                .getConnectionFromSession(new ServletWebRequest(request));

        userInfo.setProviderId(connection.getKey().getProviderId());
        userInfo.setProviderUserId(connection.getKey().getProviderUserId());
        userInfo.setNickname(connection.getDisplayName());
        userInfo.setHeadimg(connection.getImageUrl());

        return userInfo;
    }

    // 用户点击注册或者绑定，调用此接口
    @PostMapping("/user/regist")
    public Object userRegist(HttpServletRequest request, Map<String, String> map){
        // 这个注册没起效果
        providerSignInUtils.doPostSignUp(map.get("username"), new ServletWebRequest(request));
        return "成功";
    }

}
