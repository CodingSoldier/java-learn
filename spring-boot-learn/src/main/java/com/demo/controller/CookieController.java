package com.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class CookieController {


    @PostMapping("/cookie/post")
    public Map<String,Object> cookiePost(HttpServletRequest request, HttpServletResponse response) {

        Map<String,Object> r = new HashMap<String,Object>();
        r.put("token", UUID.randomUUID().toString().replaceAll("-","r"));

        return r;
    }

}
