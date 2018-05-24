package com.spring.aoplog;

import com.utils.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class T extends BaseTest{
    @Autowired
    private Ctrl1 ctrl1;

    @Test
    public void t(){
        ctrl1.testLog();
        //ctrl1.testNoLog();
    }
}
