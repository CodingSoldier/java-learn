package com.cpq.txapp01.sysuser.service;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.cpq.txapp01.sysuser.client.TxApp02Client;
import com.cpq.txapp01.sysuser.mapper.SysUserMapper;
import com.cpq.txapp01.sysuser.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SysUserServiceImpl implements SysUserService{

    @Autowired
    TxApp02Client txApp02Client;

    @Autowired
    SysUserMapper sysUserMapper;


    @Override
    @TxTransaction(isStart = true)
    @Transactional
    public JSONObject add() {

        SysUser user = new SysUser();
        user.setId(UUID.randomUUID().toString().replace("-",""));
        user.setName("name01");
        int num = sysUserMapper.insert(user);


        Object jsonObject = txApp02Client.add();


        int v = 100/0;

        return new JSONObject();
    }

}
