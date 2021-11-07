package com.cpq.shiro2.ctrl;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class AnnoController {

    /**
     * 使用注解  @RequiresRoles("admin")
     */
    @RequestMapping("/admin")
    @RequiresRoles("admin")
    public String roleAdmin() {
        return "/role/admin";
    }


}