package com.demo.testvalidate.controller;


import com.demo.config.AppProp;
import com.demo.paramsvalidate.ParamsValidate;
import com.demo.testvalidate.bean.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    AppProp appProp;

    @PostMapping("/p1/map")
    //@ParamsValidate("json-post-one.json")
    @ParamsValidate(value = "json-post-part-jackson.json", keyName = "map1")
    public Object p1(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        System.out.println(appProp.getA());
        return map;
    }

    @PostMapping("/p2/vo")
    @ParamsValidate(value = "json-post-part-jackson.json", keyName = "vo1")
    public Object p2(HttpServletRequest request, HttpServletResponse response,@RequestBody UserVo userVo) throws Exception{

        return userVo;
    }

}
