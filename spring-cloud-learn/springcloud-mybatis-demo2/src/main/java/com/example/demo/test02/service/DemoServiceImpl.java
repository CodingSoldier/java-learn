package com.example.demo.test02.service;

import com.codingapi.tx.annotation.TxTransaction;
import com.example.demo.test02.entity.Test;
import com.example.demo.test02.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Created by lorne on 2017/6/26.
 */
@Service
public class DemoServiceImpl implements DemoService{

    @Autowired
    private TestMapper testMapper;



    @Override
    public List<Test> list() {
        return testMapper.findAll();
    }


    @Override
    @Transactional
    @TxTransaction
    public int save() {

        int rs = testMapper.save(UUID.randomUUID().toString(),"mybatis-hello-2");

        return rs;
    }
}
