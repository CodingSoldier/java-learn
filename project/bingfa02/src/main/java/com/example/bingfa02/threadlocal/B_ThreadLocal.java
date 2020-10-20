package com.example.bingfa02.threadlocal;

import lombok.Data;

public class B_ThreadLocal {
}


/**
 * 同一线程多个方法之间使用共享变量
 */
@Data
class User{
    private String name;

    public User(String name) {
        this.name = name;
    }
}

class UserContextHolder{
    public static ThreadLocal<User> holder = new ThreadLocal<>();
}

class Service1{
    public void process(String name){
        User user = new User(name);
        UserContextHolder.holder.set(user);
        System.out.println("service1: "+user);
        new Service2().process();
    }
}

class Service2{
    public void process(){
        // 获取线程变量
        User user = UserContextHolder.holder.get();
        System.out.println("service2: "+user);
        new Service3().process();
    }
}

class Service3{
    public void process(){
        User user = UserContextHolder.holder.get();
        System.out.println("service3: "+user);
    }
}

class ContextHolderTest{
    public static void main(String[] args) {
        new Service1().process("名字");
    }
}







/*

static class Entry extends WeakReference<ThreadLocal<?>> {
    Object value;
    Entry(ThreadLocal<?> k, Object v) {
        super(k);
        value = v;
    }
}
ThreadLocal.ThreadLocalMap.Entry是一个弱引用
弱引用的特点：如果这个对象只被弱引用关联（没有任何强引用关联），那么这个对象就可以被回收
但是Entry只有key是弱引用，value还是一个强引用，若value还被其他变量引用着，Entry就不能被GC回收

如果线程运行完后终止了，线程中的ThreadLocal.ThreadLocalMap对象也会被回收，ThreadLocal.ThreadLocalMap.Entry当然也会被回收。

但是如果线程不终止，则会发生内存泄露。比反说，线程是线程池中的线程，则线程会被反复使用

JDK有考虑这个问题，在set、remove、rehash方法中会扫描key为null的Entry，并把对应的value设置为null，这样value对象就可以被回收
java.lang.ThreadLocal.ThreadLocalMap.resize()
    if (k == null) {
        e.value = null; // Help the GC
    }

避免内存泄露：
    调用remove方法，就会删除对应的Entry对象，可以避免内存泄露，所以使用完ThreadLocal之后，就要调用remove方法。


 */









