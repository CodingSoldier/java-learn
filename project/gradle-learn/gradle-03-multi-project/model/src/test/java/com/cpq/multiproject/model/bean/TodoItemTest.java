package com.cpq.multiproject.model.bean;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TodoItemTest {

    Logger logger = LoggerFactory.getLogger(TodoItemTest.class);

    @Test
    public void test01(){
        TodoItem todoItem = new TodoItem("dafsf", false);
        logger.info("打印 "+todoItem.toString());
    }

}
