package com.cpq.paramsvalidateboot.controller;

import com.cpq.paramsvalidateboot.bean.UserVo;
import com.cpq.paramsvalidateboot.validate.ParamsValidate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class PostController {

    @PostMapping("/p1/map")
    @ParamsValidate(file = "json-post.json")
    public Object p1(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{

        return map;
    }

    @PostMapping("/p2/vo")
    //@ParamsValidate(file = "v1/json-test.json", keyName="map01")
    public Object p2(HttpServletRequest request, HttpServletResponse response,@RequestBody UserVo userVo) throws Exception{

        return userVo;
    }

    @PostMapping("/pg3/vo")
    @ParamsValidate(file = "json-post.json")
    public Object p3(HttpServletRequest request, HttpServletResponse response,@RequestBody UserVo userVo, @RequestParam Map<String, Object> param) throws Exception{

        return new HashMap<String, Object>(){{put("vo", userVo); put("p", param);}};
    }

    @PostMapping("/pg4/json-obj")
    //@ParamsValidate(file = "json-post.json")
    public Object pg4(HttpServletRequest request, HttpServletResponse response, @RequestBody Map json, @RequestParam Map<String, Object> param) throws Exception{

        return new HashMap<String, Object>(){{put("json", json); put("p", param);}};
    }

    @RequestMapping("/form-data/vo")
    //@ParamsValidate(file = "v1/json-test.json", keyName="map01")
    public Object formData(HttpServletRequest request, @RequestParam("picFile") MultipartFile picFile,  @ModelAttribute UserVo vo) throws Exception {

        return vo;
    }

    @RequestMapping("/form-data/map")
    //@ParamsValidate(file = "v1/json-test.json", keyName="map01")
    public Object formData(HttpServletRequest request, @RequestParam("picFile") MultipartFile picFile,  @RequestParam Map<String, Object> map) throws Exception {

        return map;
    }
}
