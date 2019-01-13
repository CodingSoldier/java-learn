package com.demo.old.beanhibernate;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hibernate/auto/test")
public class TestHibernate {

    @PostMapping("/post01")
    public Object postMap(@RequestBody @Validated Dream dream, BindingResult bindingResult) throws Exception{
        if(bindingResult.hasErrors()){
            for (FieldError fieldError : bindingResult.getFieldErrors()) {

            }
            return "fail";
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", "成功");
        return map1;
    }

    @PostMapping("/post02")
    public Object postUser(@RequestBody @Validated UserVo user, BindingResult bindingResult) throws Exception{
        if(bindingResult.hasErrors()){
            for (FieldError fieldError : bindingResult.getFieldErrors()) {

            }
            return "fail";
        }
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", "成功");
        return map1;
    }

    @PostMapping("/post03")
    public Object postUser3(@RequestBody UserVo user) throws Exception{
        ValidatorUtils.validateEntity(user);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", "成功");
        return map1;
    }

}
