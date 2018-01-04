package com.spring.aop;

import com.spring.aop.c.service.CService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 http://www.cnblogs.com/jianjianyang/p/4904859.html
*/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/spring/aop/c/application-context.xml")
public class C_Aspect_Test {
    @Autowired
    CService cService;
    @Test
    public void t(){
        cService.addUser();
    }

    //方法中获取applicationContext
    @Test
    public void t2(){
        ApplicationContext ctx = new ClassPathXmlApplicationContext("/com/spring/aop/c/application-context.xml");
        CService cService = (CService)ctx.getBean("CServiceImpl");
        cService.addUser();
    }
}
