package com.example.bspringboot.m_mybatis;

import com.example.bspringboot.bean.Demo;
import com.example.bspringboot.mapper.DemoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestMybatis {

    @Autowired
    DemoMapper demoMapper;

    @Test
    public void test01(){
        Demo demo = new Demo();
        demo.setId(1L);
        demo.setJob("job");
        demo.setName("name");
        demoMapper.insert(demo);
    }

}
