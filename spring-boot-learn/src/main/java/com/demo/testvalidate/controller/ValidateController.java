package com.demo.testvalidate.controller;


import com.demo.config.AppProp;
import com.demo.paramsvalidate.ParamsValidate;
import com.demo.testvalidate.bean.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/validate")
public class ValidateController {

    @Autowired
    AppProp appProp;

    @PostMapping("/post/map")
    @ParamsValidate("json-post-one.json")
    //@ParamsValidate(value = "json-post-part-jackson.json", key = "map")
    public Object p1(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        System.out.println(appProp.getA());
        return map;
    }

    @PostMapping("/post/vo")
    @ParamsValidate(value = "json-post-part-jackson.json", key = "vo")
    public Object p2(HttpServletRequest request, HttpServletResponse response,@RequestBody UserVo userVo) throws Exception{

        return userVo;
    }

    @GetMapping("/get/id")
    @ParamsValidate(file = "json-get-one.json")
    //@ParamsValidate(file = "json-get-one.json" , key = "name")
    public Map<String, Object> g1(String name) throws Exception{
        return new HashMap<String, Object>(){{put("name",name);}};
    }

    @RequestMapping("/get/user-vo")
    @ParamsValidate(file = "json-get-part-fastjson.json")
    public Map<String, Object> vo(HttpServletRequest request, UserVo userVo) throws Exception {

        return new HashMap<String, Object>(){{put("r",userVo);}};
    }

}
