package com.cpq.multiproject.service01.service;

import com.cpq.multiproject.model.bean.TodoItem;

public class MyService {

    public TodoItem getToItem(){
        return new TodoItem("来自于service01", false);
    }

}
