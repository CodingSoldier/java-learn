package com.demo.thread;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class SpringBeanThread {

    private List<String> l2;

    private static MyThreadLocal<List<String>> listThreadLocal = new MyThreadLocal<>();

    public List<String> addList(){
        //listThreadLocal.setList(null);
        listThreadLocal.set(new ArrayList<>());

        //listThreadLocal.setList(list);
        try {
            List<String> list = listThreadLocal.get();
            list.add("1**"+new Integer(new Random().nextInt(100)).toString());
            listThreadLocal.set(list);
            Thread.yield();
            list = listThreadLocal.get();
            list.add("2**"+new Integer(new Random().nextInt(100)).toString());
            listThreadLocal.set(list);
            Thread.yield();
            list = listThreadLocal.get();
            list.add("3**"+new Integer(new Random().nextInt(100)).toString());
            listThreadLocal.set(list);
            Thread.yield();
            list = listThreadLocal.get();
            list.add("4**"+new Integer(new Random().nextInt(100)).toString());
            listThreadLocal.set(list);
            Thread.yield();
            list = listThreadLocal.get();
            list.add("5**"+new Integer(new Random().nextInt(100)).toString());
            listThreadLocal.set(list);

            return listThreadLocal.get();
        }finally {
            listThreadLocal.set(null);
        }

        //return listThreadLocal.get();
    }

    public List<String> addList2(){
        List list = new ArrayList();
        listThreadLocal.set(new ArrayList<>());
        listThreadLocal.get().add("1**"+new Integer(new Random().nextInt(100)).toString());
        Thread.yield();
        listThreadLocal.get().add("2**"+new Integer(new Random().nextInt(100)).toString());
        Thread.yield();
        listThreadLocal.get().add("3**"+new Integer(new Random().nextInt(100)).toString());
        Thread.yield();
        listThreadLocal.get().add("4**"+new Integer(new Random().nextInt(100)).toString());
        Thread.yield();
        listThreadLocal.get().add("5**"+new Integer(new Random().nextInt(100)).toString());
        list = listThreadLocal.get();
        listThreadLocal = null;
        return list;
    }

    public List<String> l2(){
        l2 = new ArrayList<>();
        l2.add("l1**"+new Integer(new Random().nextInt(100)).toString());
        Thread.yield();
        l2.add("l2**"+new Integer(new Random().nextInt(100)).toString());
        Thread.yield();
        l2.add("l3**"+new Integer(new Random().nextInt(100)).toString());
        Thread.yield();
        l2.add("l4**"+new Integer(new Random().nextInt(100)).toString());
        Thread.yield();
        l2.add("l5**"+new Integer(new Random().nextInt(100)).toString());
        return l2;
    }

}
