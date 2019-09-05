package com.example.cauth2.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 实现这个类之后就不会在控制太打印密码了
 */
@Component
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("登陆用户名：" + username);

        // 最后一项是权限
        //User user = new User(username, passwordEncoder.encode("123456"), AuthorityUtils.commaSeparatedStringToAuthorityList("admin, ROLE_USER"));

        //7参数构造函数，判断用户是否过期、密码冻结之类的
        User user = new User(username, passwordEncoder.encode("123456"),
                true, true, true, true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER"));

        return user;
    }
}
