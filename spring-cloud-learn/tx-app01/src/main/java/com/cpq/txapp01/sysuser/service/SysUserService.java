package com.cpq.txapp01.sysuser.service;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.tx.annotation.TxTransaction;
import com.cpq.txapp01.sysuser.mapper.SysUserMapper;
import com.cpq.txapp01.sysuser.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;
    @Autowired
    RestTemplate restTemplateBalanced;

    @Transactional(rollbackFor = Exception.class)
    @TxTransaction(isStart = true)
    public int add(){

        SysUser user = new SysUser();
        user.setId(UUID.randomUUID().toString().replace("-",""));
        user.setName("name01");
        int num = sysUserMapper.insert(user);

        String url = "http://tx-app02/sys/role/add";
        JSONObject json = new JSONObject();
        json.put("a1", 123456);
        JSONObject r = restTemplateBalanced.postForObject(url, json, JSONObject.class);
        if (r.getInteger("code") == 0){
            throw new RuntimeException("异常");
        }

        return num;
    }

}
