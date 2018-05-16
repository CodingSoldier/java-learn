package com.demo.controller;

import com.demo.validate.ParamsValidate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/validate")
public class ValidateController {

    @PostMapping("/post/map")
    @ParamsValidate(file = "/v1/postMap.json", keyName="map01")
    public Object request01(HttpServletRequest request, HttpServletResponse response,@RequestBody Map<String, Object> map) throws Exception{

        System.out.println(map.toString());
        return map;
    }

}
