package com.demo.old.boy.service;

import com.demo.old.boy.mapper.BoyMapper;
import com.demo.old.boy.model.Boy;
import com.demo.old.boy.model.BoyExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BoyServiceImpl implements BoyService {
    @Autowired
    BoyMapper boyMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object insert() {
        String id = UUID.randomUUID().toString();
        //String name = "123423447";
        Boy boy = new Boy();
        boy.setId(id);
        boy.setSmallName("123423447");
        boy.setBigName("安大明");
        boyMapper.insertSelective(boy);

        BoyExample example = new BoyExample();
        BoyExample.Criteria criteria = example.createCriteria();
        criteria.andSmallNameEqualTo("123423447");
        Boy bName = boyMapper.selectByExample(example).get(0);
        System.out.println("通过其他条件查询："+bName);

        //Boy bId = boyMapper.selectByPrimaryKey(id);
        //System.out.println("通过id查询："+bId);

        return bName;
    }
}
