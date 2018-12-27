package com.cpq.txapp02.sysrole.service;

import com.codingapi.tx.annotation.TxTransaction;
import com.cpq.txapp02.sysrole.mapper.SysRoleMapper;
import com.cpq.txapp02.sysrole.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SysRoleServiceImpl implements SysRoleService{

    @Autowired
    SysRoleMapper sysRoleMapper;

    @Override
    @Transactional
    @TxTransaction
    public int add(){

        SysRole role = new SysRole();
        role.setId(UUID.randomUUID().toString().replace("-",""));
        role.setName("name01");
        int num = sysRoleMapper.insert(role);
        System.out.println("***********已执行*************");
        return num;
    }
}
