package com.demo.controller;

import com.demo.validate.ParamsValidate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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

    @RequestMapping(value = {"/post/path/{id}", "/post/path/{id2}"})
    @ParamsValidate(file = "/v1/postMap.json", keyName="map01")
    public Object pathmore(HttpServletRequest request, @PathVariable Integer id, @RequestParam String name, @RequestBody Map<String, String> map) throws Exception{

        System.out.println(request.getParameterMap());
        System.out.println(request.getParameterNames());
        return new Object();
    }

    @GetMapping("/get/param1")
    @ParamsValidate(file = "/v1/postMap.json", keyName="map01")
    public Object param1(String p2) throws Exception{

        return new Object();
    }

    @GetMapping("/get/no/request")
    //@ParamsValidate(file = "/v1/postMap.json", keyName="map01")
    public Object noRequest(String p2, @RequestParam List<String> l1) throws Exception{

        return new Object();
    }

    @GetMapping("/get/more")
    @ParamsValidate(file = "/v1/postMap.json", keyName="map01")
    public Object getmore(HttpServletRequest request, @RequestParam List<String> l1) throws Exception{

        System.out.println(l1);
        System.out.println(request.getParameterMap());
        System.out.println(request.getParameterNames());
        return new Object();
    }

    @GetMapping("/get/map")
    @ParamsValidate(file = "/v1/postMap.json", keyName="map01")
    public Object request02(HttpServletRequest request, @RequestParam Map<String, Object> map) throws Exception{

        System.out.println(request.getParameterMap());
        System.out.println(request.getParameterNames());
        return new Object();
    }


    @GetMapping("/get/path/{id}")
    @ParamsValidate(file = "/v1/postMap.json", keyName="map01")
    public Object path(HttpServletRequest request, @PathVariable Integer id, @RequestParam String name) throws Exception{

        System.out.println(request.getParameterMap());
        System.out.println(request.getParameterNames());
        return new Object();
    }

    @RequestMapping("/formData")
    @ResponseBody
    @ParamsValidate(file = "/v1/postMap.json", keyName="map01")
    public Object formData(HttpServletRequest request, @RequestParam("picFile") MultipartFile picFile, Map<String, Object> imgTest) throws Exception {

        return new Object();
    }

}
