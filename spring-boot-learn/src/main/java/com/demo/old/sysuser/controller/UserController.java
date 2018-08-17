package com.demo.old.sysuser.controller;

import com.demo.old.sysuser.model.SysUser;
import com.demo.old.sysuser.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysuser")
public class UserController {

    @Autowired
    SysUserService service;

    @GetMapping("/t1")
    public String helloworld2() throws Exception{
        SysUser sysUser = new SysUser();
        sysUser.setId("11111");
        sysUser.setName("dsadfa");
        service.updateByPrimaryKeySelective(sysUser);
        return "helloworld2";
    }


}
