package com.cpq.txapp02.sysrole.controller;

import com.alibaba.fastjson.JSONObject;
import com.cpq.txapp02.sysrole.client.TxApp01;
import com.cpq.txapp02.sysrole.service.SysRoleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
    @Autowired
    TxApp01 txApp01;

    @Autowired
    SysRoleServiceImpl sysRoleService;

    @PostMapping("/add")
    public JSONObject add(HttpServletRequest request) throws Exception{
        sysRoleService.add();
        System.out.println("tx-app02已经执行");
        JSONObject json = new JSONObject();
        json.put("code", 0);
        return json;
    }

}
