package com.cpq.shiro2.ctrl;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/definition")
public class DynamicDefinitionController {

    @Autowired
    ShiroFilterFactoryBean shiroFilterFactoryBean;

    /**
     * 使动态修改definition
     */
    @GetMapping("/change")
    @RequiresRoles("admin")
    public String change() {
        System.out.println(shiroFilterFactoryBean.getFilterChainDefinitionMap().toString());
        shiroFilterFactoryBean.getFilterChainDefinitionMap().put("/my/test", "role[admin]");
        return "/definition";
    }

    @GetMapping("/my/test")
    public String myadd() {
        return "/my/test";
    }


}