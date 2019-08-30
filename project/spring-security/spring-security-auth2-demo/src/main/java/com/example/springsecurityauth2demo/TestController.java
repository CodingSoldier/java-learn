package com.example.springsecurityauth2demo;

import com.example.springsecurityauth2demo.security.SSOUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-08-30
 */
@RestController
@RequestMapping("/test")
public class TestController {

    // 不需要认证token的资源
    @GetMapping("/no/token/permit")
    public Object noTokenPermit(){
        return "访问成功/no/token/permit";
    }

    // 需要认证token的资源
    @GetMapping("/verify/token/01")
    public Object verifyToken01(){

        //获取token中的用户信息
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        SSOUserDetails ssoUserDetails = (SSOUserDetails) user.getPrincipal();
        System.out.println(ssoUserDetails.toString());

        return "访问成功，verifyToken01 ";
    }

}
