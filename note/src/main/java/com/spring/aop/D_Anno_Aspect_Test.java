package com.spring.aop;

import com.spring.aop.d.controller.DCtrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义注解方式实现aop
 * http://www.cnblogs.com/jianjianyang/p/4910851.html
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/spring/aop/d/application-context.xml")
public class D_Anno_Aspect_Test {
    @Autowired
    DCtrl dCtrl;

    @Test
    public void t(){
        Map<String, Object> r = dCtrl.add(null,new HashMap<String, Object>());
        System.out.println(r.toString());
    }
}
