package com.cpq.txapp01.sysuser.service;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.ITxTransaction;
import com.codingapi.tx.annotation.TxTransaction;
import com.cpq.txapp01.sysuser.client.Demo1Client;
import com.cpq.txapp01.sysuser.client.TxApp02Client;
import com.cpq.txapp01.sysuser.mapper.SysUserMapper;
import com.cpq.txapp01.sysuser.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class SysUserServiceImpl implements SysUserService, ITxTransaction {

    @Autowired
    private TxApp02Client txApp02Client;
    @Autowired
    private Demo1Client demo1Client;
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

    @Override
    @TxTransaction(isStart = true)
    @Transactional
    public JSONObject app01app02demo1demo2() {

        SysUser user = new SysUser();
        user.setId(UUID.randomUUID().toString().replace("-",""));
        user.setName("app01app02demo1demo2");
        int num = sysUserMapper.insert(user);
        System.out.println("执行----com.cpq.txapp01.sysuser.service.SysUserServiceImpl.app01app02demo1demo2");

        demo1Client.save();

        txApp02Client.add();

        //这里抛异常，前面的代码都回滚
        int v = 100/0;

        return new JSONObject();
    }

}
