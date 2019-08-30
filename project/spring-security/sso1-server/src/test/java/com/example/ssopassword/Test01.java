package com.example.ssopassword;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class Test01 {

    @Test
    public void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //加密"0"
        String encode1 = bCryptPasswordEncoder.encode("123dfadfas");
        System.out.println(encode1);
        String encode2 = bCryptPasswordEncoder.encode("123dfadfas");
        System.out.println(encode2);
        System.out.println(bCryptPasswordEncoder.matches("123dfadfas", encode1));
        System.out.println(bCryptPasswordEncoder.matches("123dfadfas", encode2));

    }

}
