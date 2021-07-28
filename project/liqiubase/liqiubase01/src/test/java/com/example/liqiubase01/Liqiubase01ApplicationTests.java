package com.example.liqiubase01;

import com.example.liqiubase01.test01.entity.Test01;
import com.example.liqiubase01.test01.service.Test01Service;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Liqiubase01ApplicationTests {

    @Autowired
    Test01Service test01Service;

    @Test
    public void contextLoads() {
        List<Test01> list = test01Service.list();
        System.out.println(list);
    }

}
