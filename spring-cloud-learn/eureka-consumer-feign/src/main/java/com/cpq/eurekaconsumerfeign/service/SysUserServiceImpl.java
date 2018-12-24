package com.cpq.eurekaconsumerfeign.service;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.cpq.eurekaconsumerfeign.mapper.SysUserMapper;
import com.cpq.eurekaconsumerfeign.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
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

        Map<String, Object> map = new HashMap<>();
        Object jsonObject = txApp02Client.add();


        int v = 100/0;

        return new JSONObject();
    }

}
