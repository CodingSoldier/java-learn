package com.example.esecurity.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限控制测试
 */
@RestController
public class SecurityController {

    @GetMapping("/security/role/admin")
    public Object test(){
        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);
        result.put("message", "访问成功");
        result.put("user", SecurityContextHolder.getContext().getAuthentication());
        return result;
    }

    @GetMapping("/security/role/user")
    public Object user(){
        Map<String, Object> result = new HashMap<>();
        result.put("status", 0);
        result.put("message", "访问成功");
        result.put("user", SecurityContextHolder.getContext().getAuthentication());
        return result;
    }


}
