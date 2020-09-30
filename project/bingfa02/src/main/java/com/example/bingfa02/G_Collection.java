package com.example.bingfa02;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class G_Collection {
}
/*
ConcurrentHashMap:  线程安全的HashMap
CopyOnWriteArrayList:  线程安全的List
BlockingQueue:  这是一个接口，表示阻塞队列，非常适合用于作为数据共享的通道
ConcurrentLinkedQueue:  高效的非阻塞并发队列，使用链表实现。可以看做一个线程安全的LinkedList
ConcurrentSkipListMap:  是一个Map，使用跳表的数据结构进行快速查找

 */


class Map01{

    public static void main(String[] args) {
        /**
         * HashMap的key允许为null
         */
        HashMap map = new HashMap();
        map.put(null, "HashMap");
        map.put("HashMap", null);
        System.out.println(map.get(null));
        System.out.println(map.get("HashMap"));

        /**
         * null也有哈希值，java将null的哈希值认为是0
         */
        int hash = Objects.hash(null);
        System.out.println(hash);

        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put(null, "linkedHashMap");
        linkedHashMap.put("linkedHashMap", null);
        System.out.println(linkedHashMap.get(null));
        System.out.println(linkedHashMap.get("linkedHashMap"));
    }
}


/**
 Java7中的ConcurrentHashMap最外层是多个segment，每个segment的底层数据结构与HashMap类似，任然是数组，数组元素是一个链表
 每个segment独立使用ReentrantLock，每个segment之间互不影响，提高了并发效率
 ConcurrentHashMap默认有16个segment，所以最多可以同时支持16个线程并发写（操作分别分布在不同的segment上）。这个默认值可以在初始化的时候设置为其他值，但是一旦初始化之后就不可以扩容

 java8的ConcurrentHashMap取消了segment，使用了HashMap的结构，并且使用了CAS + synchronized

 */


























