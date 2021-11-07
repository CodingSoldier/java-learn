package com.cpq.shiro2.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/any/role")
public class RoleAnyFilterController {

    @RequestMapping("/123")
    public String roleAdmin() {
        return "/123";
    }


}