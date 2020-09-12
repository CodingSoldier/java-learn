package com.cpq.multiproject.service01.service;

import com.cpq.multiproject.model.bean.TodoItem;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MyServiceTest {

    Logger logger = LoggerFactory.getLogger(MyServiceTest.class);

    @Test
    public void test01(){
        MyService myService = new MyService();
        TodoItem toItem = myService.getToItem();
        logger.info("######"+toItem.toString());
    }

}
