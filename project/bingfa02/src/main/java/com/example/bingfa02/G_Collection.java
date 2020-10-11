package com.example.bingfa02;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.*;

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


/**
 * ConcurrentHashMap组合操作不保证线程安全
 */
class OptionsNotSage implements Runnable{

    private static ConcurrentHashMap<String, Integer> scores = new ConcurrentHashMap<>();

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {

            //// 线程安全的写法
            //while (true){
            //    Integer score = scores.get("小明");
            //    Integer newScore = score + 1;
            //    // replace，如果key（小明）对应的value是score，则替换为newScore
            //    boolean b = scores.replace("小明", score, newScore);
            //    if (b){
            //        break;
            //    }
            //}

            /**
             * 组合操作，线程不安全
             * ConcurrentHashMap只是源码内部保证线程安全，使用者要保证自己的代码也是线程安全的
             */
            Integer score = scores.get("小明");
            Integer newScore = score + 1;
            scores.put("小明", newScore);

        }
    }

    public static void main(String[] args) throws Exception{
        scores.put("小明", 0);
        Thread t1 = new Thread(new OptionsNotSage());
        Thread t2 = new Thread(new OptionsNotSage());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(scores);
    }

}


/*
CopyOnWriteArrayList
    修改列表的时候先把列表拷贝一份，对拷贝进行修改，修改完成后把指针指向修改后的列表

CopyOnWriteArrayList适用场景
    读操作可以尽可能地快，而写即使慢一些也没有太大关系

CopyOnWriteArrayList的读写规则：
    读操作完全不用加锁，写操作不会阻塞读操作
    写操作和写操作之间需要进行同步等待

// CopyOnWriteArrayList源码，添加操作
public boolean add(E e) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        // 获取到数组
        Object[] elements = getArray();
        int len = elements.length;
        // 拷贝出一个新的数组
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        // 添加最后一个元素
        newElements[len] = e;
        // 指针指向新数组
        setArray(newElements);
        return true;
    } finally {
        lock.unlock();
    }
}
 */

class CopyOnWriteArrayListDemo1{

    public static void main(String[] args) {
        // ArrayList不能再遍历的时候修改列表
        //ArrayList<String> list = new ArrayList<>();

        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        Iterator<String> iterator = list.iterator();

        /**
         * CopyOnWriteArrayList可以在遍历的时候修改列表
         * 但是有个问题，即便是在同一个线程中，遍历的时候修改列表，遍历得到的元素还是旧列表的元素，修改不会影响遍历
         */
        while (iterator.hasNext()){
            System.out.println("list是 "+list);
            String next = iterator.next();
            System.out.println("next元素是 "+next);

            if (next.equals("2")){
                System.out.println("删除5");
                list.remove("5");
            }
            if (next.equals("3")){
                System.out.println("添加新元素new");
                list.add("new");
            }
        }
    }

}


/**
 * 阻塞队列生产者消费者
 */
// 生产者
class Interviewer implements Runnable{
    BlockingQueue<String> queue;
    public Interviewer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        System.out.println("10个候选人都来了");
        for (int i = 0; i < 10; i++) {
            String candidate = "Candidate" + i;
            try {
                queue.put(candidate);
                System.out.println(candidate+"准备好了");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        try {
            queue.put("stop");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
// 消费者
class Consumer implements Runnable{
    BlockingQueue<String> queue;
    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String candidate;
        try {
            while (!"stop".equals((candidate = queue.take()))){
                System.out.println(candidate+"面试中");
                TimeUnit.SECONDS.sleep(1);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("面试全部结束");
    }
}

class ArrayBlockingQueueDemo{
    public static void main(String[] args) {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(3);
        Interviewer interviewer = new Interviewer(queue);
        Consumer consumer = new Consumer(queue);
        new Thread(interviewer).start();
        new Thread(consumer).start();
    }
}














