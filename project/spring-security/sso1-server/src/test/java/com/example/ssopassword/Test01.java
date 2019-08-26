package com.example.ssopassword;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class Test01 {

    @Test
    public void contextLoads() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        //加密"0"
        String encode = bCryptPasswordEncoder.encode("0");
        System.out.println(encode);
    //  $2a$10$iyVj57zh63lG1KIIXVP0BObYl1IBYcAvdmi5.upqIit7HvHj82nT6
    }

}
