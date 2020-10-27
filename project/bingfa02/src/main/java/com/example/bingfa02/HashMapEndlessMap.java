package com.example.bingfa02;

import java.util.HashMap;

/**
 *
 * @author chenpiqian
 * @date: 2020-09-30
 */
public class HashMapEndlessMap {

    private static HashMap<Integer, String> map = new HashMap<>(2, 1.5f);

    /**
     必须使用jdk7
      java.util.HashMap#transfer(java.util.HashMap.Entry[], boolean)
            if (rehash) {   此处打一个线程断点，运行本代码就会出现线程2堆内存溢出

     原因： https://coolshell.cn/articles/9606.html

     我们新建了槽位数为2的hashmap，则key为5,7,3的键值对在槽1
     因为java7 hashmap对链表的插入时在头部插入
     map.put(5, "C");
     map.put(7, "B");
     map.put(3, "A");
     执行完上面的代码后
     链表是
     3 指向 7 指向 5
     扩容发生，两个线程同时执行，且刚好3和7还在同一个槽位
     线程1扩容遍历链表的时候 运行到 3指向7 暂停
     线程2完成扩容，由于对链表的插入是在头部插入，链表变成了 7指向3
     线程1继续执行，遍历3指向7，继续遍历，由于线程2的结果，7又指向了3,
     造成环形链表死循环
     */
    public static void main(String[] args) {
        map.put(5, "C");
        map.put(7, "B");
        map.put(3, "A");

        new Thread(new Runnable() {
            @Override
            public void run() {
                map.put(15, "D");
                System.out.println(map);
            }
        }, "线程1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                map.put(1, "E");
                System.out.println(map);
            }
        }, "线程2").start();

    }

}
