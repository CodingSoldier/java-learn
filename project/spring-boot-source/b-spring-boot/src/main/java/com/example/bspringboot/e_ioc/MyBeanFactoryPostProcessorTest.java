package com.example.bspringboot.e_ioc;

import com.example.bspringboot.e_ioc.bean.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MyBeanFactoryPostProcessorTest {

    @Autowired
    Teacher teacher;

    @Test
    void test01() {
        System.out.println(teacher.getName());
    }

}
