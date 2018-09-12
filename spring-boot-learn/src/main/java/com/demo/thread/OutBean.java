package com.demo.thread;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutBean {
    @Autowired
    SpringBeanThread springBeanThread;

    public List<String> test(){
        return springBeanThread.addList();
    }

    public List<String> test2(){
        return springBeanThread.addList2();
    }

    public List<String> l2(){
        return springBeanThread.l2();
    }
}
