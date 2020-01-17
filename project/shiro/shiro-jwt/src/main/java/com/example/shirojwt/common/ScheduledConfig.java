package com.example.shirojwt.common;

import com.example.shirojwt.JwtRealm;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@Slf4j
class ScheduledConfig {

    @Autowired
    JwtRealm jwtRealm;

    /**
     * 每天凌晨1点清除jwtRealm中的缓存
     */
    //@Scheduled(cron="0 */1 * * * ?")
    @Scheduled(cron = "0 0 1 * * ?")
    public void clearJwtRealmCache(){

        Cache<Object, AuthenticationInfo> authen = jwtRealm.getAuthenticationCache();
        Cache<Object, AuthorizationInfo> author = jwtRealm.getAuthorizationCache();
        log.info("当前缓存, 认证缓存 = {} 授权缓存 = {}", authen, author);

        authen.clear();
        author.clear();
        log.info("缓存清除完成，认证缓存size = {} 授权缓存size = {}", authen.size(), author.size());
    }

}
