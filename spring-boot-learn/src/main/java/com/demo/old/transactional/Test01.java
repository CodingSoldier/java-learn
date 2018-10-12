package com.demo.old.transactional;


import com.demo.SpringbootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootApplication.class)
public class Test01{

    @Autowired
    TS01 service;

    @Test
    public void select01() throws Exception{
        service.select01();
    }

    @Test
    public void update01(){
        service.update01();
    }

    @Test
    public void update222(){
        service.update222();
    }

}
