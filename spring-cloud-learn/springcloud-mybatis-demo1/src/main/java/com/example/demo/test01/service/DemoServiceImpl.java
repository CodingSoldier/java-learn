package com.example.demo.test01.service;

import com.codingapi.tx.annotation.TxTransaction;
import com.example.demo.test01.client.Demo2Client;
import com.example.demo.test01.mapper.TestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lorne on 2017/6/26.
 */
@Service
public class DemoServiceImpl implements DemoService {

    private Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private Demo2Client demo2Client;

    @Autowired
    private TestMapper testMapper;

    @Override
    @TxTransaction(isStart = true)
    @Transactional
    public int save() {
        int rs1 = testMapper.save("mybatis-hello-1");
        int rs2 = demo2Client.save();
        int v = 100/0;
        return rs1+rs2;
    }

    @Override
    @TxTransaction(isStart = true)
    @Transactional
    public int save2() {
        int rs1 = testMapper.save("222222222");
        System.out.println("执行----com.example.demo.test01.service.DemoServiceImpl.save2");
        int rs2 = demo2Client.save();
        return rs1+rs2;
    }
}
