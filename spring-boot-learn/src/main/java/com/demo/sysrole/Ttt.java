package com.demo.sysrole;

import com.demo.sysrole.mapper.SysRoleMapper;
import com.demo.sysrole.model.SysRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Ttt {

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Test
    public void test1(){
        SysRole role = new SysRole();
        role.setId("0001");
        role.setName("");
        role.setSysType(1);
        sysRoleMapper.updateByPrimaryKeySelective(role);
    }
}
