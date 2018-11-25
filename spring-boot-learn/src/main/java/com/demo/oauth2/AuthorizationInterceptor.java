package com.demo.oauth2;

import com.demo.old.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisService redisService;

    //@Override
    //public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //
    //    //排除获取token接口
    //    String uri = request.getRequestURI();
    //    if (uri.equals("/oauth2/client/get/token")){
    //        return true;
    //    }
    //
    //    //校验token权限
    //    boolean pass = false;
    //    String access_token = request.getParameter("access_token");
    //    if (StringUtils.isBlank(access_token)){
    //        throw new Exception("access_token为空");
    //    }
    //
    //    String tokenKey = OAUTH2_CLIENT_TOKEN + access_token;
    //    if (!redisService.hasKey(tokenKey)){
    //        throw new Exception("token过期");
    //    }
    //    Set<String> accessRegexSet = redisService.getCollectionSet(tokenKey);
    //    for (String accessRegex:accessRegexSet){
    //        if (StringUtils.isNotEmpty(accessRegex) && uri.matches(accessRegex)){
    //            pass = true;
    //            break;
    //        }
    //    }
    //
    //    //不允许访问
    //    if (!pass){
    //        throw new Exception("无权限访问此资源");
    //    }
    //
    //    return pass;
    //}

}
