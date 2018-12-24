package com.cpq.txapp02.sysrole.controller;

import com.alibaba.fastjson.JSONObject;
import com.cpq.txapp02.sysrole.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {
    @Autowired
    DiscoveryClient discoveryClient;

    @Autowired
    SysRoleService sysRoleService;

    @PostMapping("/add")
    public JSONObject add(HttpServletRequest request, @RequestBody JSONObject jsonObject) throws Exception{

        String services = "txapp02-----Services: " + discoveryClient.getServices();
        System.out.println(request.getServerPort());
        System.out.println(services);

        System.out.println(jsonObject.getString("a1"));

        sysRoleService.add();

        JSONObject json = new JSONObject();
        json.put("code", 0);

        return json;
    }

}
