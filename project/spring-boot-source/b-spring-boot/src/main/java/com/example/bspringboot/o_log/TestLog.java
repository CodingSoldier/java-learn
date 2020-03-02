package com.example.bspringboot.o_log;

import com.example.bspringboot.bean.Demo;
import com.example.bspringboot.mapper.DemoMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestLog {

    private Logger LOGGER = LoggerFactory.getLogger(TestLog.class);

    @Autowired
    DemoMapper demoMapper;

    @Test
    public void test00(){
        LOGGER.info("日志debug");
        demoMapper.selectByPrimaryKey(1L);
    }

    @Test
    public void test01(){
        Demo demo = new Demo();
        demo.setId(1L);
        demo.setJob("job");
        demo.setName("name");
        demoMapper.insert(demo);
    }

}
