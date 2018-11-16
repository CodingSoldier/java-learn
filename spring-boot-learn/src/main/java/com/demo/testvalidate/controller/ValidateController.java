package com.demo.testvalidate.controller;


import com.demo.paramsvalidate.ParamsValidate;
import com.demo.testvalidate.bean.UserVo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/validate")
public class ValidateController {

    @PostMapping("/post/map")
    @ParamsValidate("json-post-one.json")
    //@ParamsValidate(value = "json-post-part-jackson.json", key = "map")
    public Object p1(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", map);
        return map1;
    }

    @PostMapping("/post/vo")
    @ParamsValidate(value = "json-post-part-jackson.json", key = "vo")
    public Object p2(HttpServletRequest request, HttpServletResponse response,@RequestBody UserVo userVo) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", userVo);
        return map1;
    }

    @PostMapping("/post/map/part")
    @ParamsValidate(value = "json-post-part-jackson.json", key = "map")
    public Object p3(HttpServletRequest request, HttpServletResponse response,@RequestBody UserVo userVo) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", userVo);
        return map1;
    }

    @GetMapping("/get/id")
    @ParamsValidate(file = "json-get-one.json")
    //@ParamsValidate(file = "json-get-one.json" , key = "name")
    public Map<String, Object> g1(String name) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", name);
        return map1;
    }

    @RequestMapping("/get/user-vo")
    @ParamsValidate(file = "json-get-part-fastjson.json")
    public Map<String, Object> vo(HttpServletRequest request, UserVo userVo) throws Exception {
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", userVo);
        return map1;
    }

}
