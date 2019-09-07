package com.example.cauth2.social;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

/**
 * spring-social通过userId校验用户
 */
@Component
@Slf4j
public class CustomSocialUserDetailsService implements SocialUserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        log.info("登陆用户名：" + userId);

        // 最后一项是权限
        SocialUser user = new SocialUser(userId, passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        //7参数构造函数，判断用户是否过期、密码冻结之类的
        //user = new User(username, passwordEncoder.encode("123456"),
        //        true, true, true, true,
        //        AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        return user;
    }
}
