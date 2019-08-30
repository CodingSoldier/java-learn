package com.example.sso1server.sso.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @Auther: wangyi
 * @Date: 2019/3/21 11:24
 * @Description:
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("username");
        //加密"0"
        String passwordEncode = passwordEncoder.encode("password");
        sysUser.setPassword(passwordEncode);
        return sysUser;
    }


}
