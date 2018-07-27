package com.thinkinginjavablog.callback;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Test01<T> {

    public <T> void addNum(T list, Callback<T> callback){
        System.out.println("回调开始");

        callback.callbackFn(list);

        System.out.println("回调结束");
    }

    @Test
    public void testFn01(){

        List<Integer> list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println("原数组："+list.toString());

        //new出Callback的匿名实例，方法callbackFn就相当于是回调函数
        addNum(list, new Callback<List<Integer>>() {
            @Override
            public void callbackFn(List<Integer> l) {
                if (l != null){
                    for (int i=0; i<l.size(); i++){
                        l.set(i, l.get(i)+1);
                    }
                }
                System.out.println("list加1后： "+list.toString());
            }
        });


        //使用lambda回调，这就有点像写JavaScript回调的感觉了
        addNum(list, (List<Integer> l) -> {
            if (l != null){
                for (int i=0; i<l.size(); i++){
                    l.set(i, l.get(i) - 2);
                }
            }
            System.out.println("list减2后： "+list.toString());
        });

    }
}















