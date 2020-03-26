package com.example.multidatasource;

import com.example.multidatasource.cpq.girl.entity.Girl;
import com.example.multidatasource.cpq.girl.service.GirlService;
import com.example.multidatasource.shiro.role.entity.Role;
import com.example.multidatasource.shiro.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommonService {

    @Autowired
    GirlService girlService;
    @Autowired
    RoleService roleService;

    @Transactional(rollbackFor = Exception.class)
    public boolean save(String msg){
        Girl girl = new Girl();
        girl.setName("name "+msg);
        boolean b1 = girlService.save(girl);

        Role role = new Role();
        role.setRoleName("role-name"+msg);
        boolean b2 = roleService.save(role);
        //
        //if (b2){
        //    throw new RuntimeException("RuntimeException");
        //}

        return b1 && b2;
    }

}
