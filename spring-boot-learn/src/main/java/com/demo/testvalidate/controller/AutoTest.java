package com.demo.testvalidate.controller;

import com.demo.paramsvalidate.ParamsValidate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auto/test")
public class AutoTest {
    @PostMapping("/post01")
    @ParamsValidate(value = "/autotest/validate-file.json", key = "all01")
    public Object postMap(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, Object> map) throws Exception{
        Map<String, Object> map1 = new HashMap<>();
        map1.put("code", 0);
        map1.put("data", "成功");
        return map1;
    }
}
