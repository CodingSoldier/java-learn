package com.cpq.txapp02.sysrole.service;

import com.codingapi.tx.annotation.TxTransaction;
import com.cpq.txapp02.sysrole.mapper.SysRoleMapper;
import com.cpq.txapp02.sysrole.model.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class SysRoleService {

    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    RestTemplate restTemplateBalanced;

    @Transactional
    @TxTransaction
    public int add(){

        SysRole role = new SysRole();
        role.setId(UUID.randomUUID().toString().replace("-",""));
        role.setName("name01");
        int num = sysRoleMapper.insert(role);

        return num;
    }

}
