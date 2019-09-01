package com.example.codepassowrd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求需要校验，跳转到这里
 */
@RestController
public class AuthenticationController {

    @GetMapping("/authentication/request")
    public Object authenticationRequest(){
        return "无权访问此资源";
    }

}
