package com.cpq.txapp01.sysuser.controller;

import com.alibaba.fastjson.JSONObject;
import com.cpq.txapp01.sysuser.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;

    @ApiOperation(value = "新增user", notes = "参数")
    @PostMapping("/add")
    public JSONObject add(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws Exception{

        sysUserService.add();

        JSONObject json = new JSONObject();
        json.put("code", 0);

        return json;
    }

}
