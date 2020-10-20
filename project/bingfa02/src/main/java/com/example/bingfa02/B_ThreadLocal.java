package com.example.bingfa02;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class B_ThreadLocal {
}

class ThreadLocalDateFormat{

    /**
     * ThreadLocal设置初始值。
     * 每次执行dateFormatLocal.get()都会执行ThreadLocal#initialValue()得到一个刚创建的SimpleDateFormat对象
     *
     * 这类似一种延迟加载的功能，在调用方法的时候才创建对象、返回对象
     */
    public static ThreadLocal<SimpleDateFormat> dateFormat1 = new ThreadLocal(){
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * lambda方式初始话初值
     */
    public static ThreadLocal<SimpleDateFormat> dateFormat2 = ThreadLocal
            .withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

}

/**
 * 利用ThreadLocal给每个线程分配自己的dateFormatter对象，保证线程安全
 */
class ThreadLocalTest01{
    static int num = 1000;
    public static ExecutorService threadPool = Executors.newFixedThreadPool(num);

    public static void main(String[] args) throws Exception{

        // 多线程使用会有bug，导致打印的时间相同
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < num; i++) {
            int temp = i;
            threadPool.submit(() -> {

                /*
                使用ThreadLocal每个线程使用自己单独的时间格式对象

                get()方法源码：
                    public T get() {
                        Thread t = Thread.currentThread();
                        ThreadLocalMap map = getMap(t);
                        if (map != null) {
                            ThreadLocalMap.Entry e = map.getEntry(this);
                            if (e != null) {
                                @SuppressWarnings("unchecked")
                                T result = (T)e.value;
                                return result;
                            }
                        }

                        get()方法前没执行过ThreadLocal的set()方法，会走到这里
                        setInitialValue()最终会执行initialValue()方法，并返回值
                        return setInitialValue();
                    }

                 */
                //SimpleDateFormat simpleDateFormat = ThreadLocalDateFormat.dateFormat1.get();
                SimpleDateFormat simpleDateFormat = ThreadLocalDateFormat.dateFormat2.get();

                String timeStr = simpleDateFormat.format(new Date(1000 * temp));
                System.out.println(timeStr);
            });
        }
    }

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
        // 设置线程变量
        /*
         ThreadLocal有一个内部类ThreadLocalMap
            ThreadLocalMap内部的Entry是一个key-value
                更加骚的是，Entry的key是ThreadLocal类型

         Thread有一个属性threadLocals 是 ThreadLocal.ThreadLocalMap 类型

         当执行 UserContextHolder.holder.set(user); 时
         UserContextHolder.holder这个ThreadLocal并不存储user变量，这点很骚
         UserContextHolder.holder执行Thread.currentThread();获取到当前线程
         再拿线程的threadLocals变量，t.threadLocals
         然后threadLocals.set(UserContextHolder.holder对象, user变量)

         UserContextHolder.holder虽然执行set方法，但实际上UserContextHolder.holder本身只作为一个key
         在set方法内部调用线程t.threadLocals的set方法，把自己（UserContextHolder.holder）作为一个key，value是user

         在UserContextHolder.holder.set(user);前后打断点，user存储在线程中，而不是在ThreadLocal中
         */
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

        // 最后要移除线程变量，房子UserContextHolder.holder过大发生一个长时间GC
        UserContextHolder.holder.remove();
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
弱引用的特点：如果这个对象只被弱引用关联（有没任何强引用关联），那么这个对象就可以被回收
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









