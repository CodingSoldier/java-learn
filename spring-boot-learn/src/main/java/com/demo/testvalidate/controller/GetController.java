package com.demo.testvalidate.controller;


import com.demo.paramsvalidate.ParamsValidate;
import com.demo.testvalidate.bean.UserVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/get")
public class GetController {

    @GetMapping("/g1")
    @ParamsValidate(file = "json-get.json", keyName="id")
    public Map<String, Object> g1(String id) throws Exception{

        return new HashMap<String, Object>(){{put("d",id);}};
    }



    @RequestMapping("/user-vo")
    @ParamsValidate(file = "json-get.json")
    public Map<String, Object> vo(HttpServletRequest request, UserVo userVo) throws Exception {

        return new HashMap<String, Object>(){{put("r",userVo);}};
    }

    @RequestMapping("/attr")
    @ParamsValidate(file = "json-get.json")
    public Map<String, Object> attr(HttpServletRequest request,@ModelAttribute UserVo userVo) throws Exception {

        return new HashMap<String, Object>(){{put("r",userVo);}};
    }
}
