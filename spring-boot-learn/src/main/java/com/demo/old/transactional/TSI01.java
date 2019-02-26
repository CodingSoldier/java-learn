package com.demo.old.transactional;

import com.demo.old.sysrole.mapper.SysRoleMapper;
import com.demo.old.sysrole.model.SysRole;
import com.demo.old.sysrole.model.SysRoleExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class TSI01 implements TS01 {

    @Autowired
    SysRoleMapper mapper;

    @Override
    public void select01() throws Exception{
        SysRoleExample example = new SysRoleExample();
        mapper.selectByExample(example);
        System.out.println("*****************select01");
        TimeUnit.SECONDS.sleep(10000L);
    }

    @Override
    @Transactional
    public void update01() throws Exception{
        SysRole sysRole= new SysRole();
        sysRole.setId("0002");
        sysRole.setName("22222222");
        mapper.updateByPrimaryKeySelective(sysRole);
        if (sysRole != null){
            throw new RuntimeException("异常");
        }
        System.out.println("*****************update01");
    }

    @Override
    @Transactional
    public void update222(){
        //SysUser user = new SysUser();
        //user.setId("11111");
        //user.setName("q1111");
        //mapper.updateByPrimaryKeySelective(user);
        //System.out.println("update222  updateByPrimaryKeySelective");
    }


}
