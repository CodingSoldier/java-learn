package com.example.bingfa02;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class F_Final {
}

/*
把变量写在线程内部——栈封闭技术
    在方法里新建的局部变量，实际上是存储在每个线程私有的栈空间，而每个栈的栈空间是不能被其他线程所访问到的，所以不会有线程安全问题。这就是著名的“栈封闭”技术，是“线程封闭”技术的一种情况。

 */
class StackConfinement implements Runnable {

    int index = 0;

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            index++;
        }


        int neverGoOut = 0;
        for (int i = 0; i < 10000; i++) {
            neverGoOut++;
        }
        System.out.println("栈内保护的数字是线程安全的：" + neverGoOut);
    }

    public static void main(String[] args) throws InterruptedException {
        StackConfinement r1 = new StackConfinement();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(r1);
        }

        executorService.shutdown();
        while (!executorService.isTerminated()){}

        System.out.println("同一对象的属性被多个线程修改，不安全："+r1.index);
    }
}


/**
 * 面试题
 */
class FinalString{
    public static void main(String[] args) {
        String a = "悟空2";
        String b = "悟空2";
        String c = "悟空"+"2";
        // 字符串字面量存储在常量池中
        System.out.println(a == b);
        // c是字面量+字面量，在编译期就能确定，所以也是存储在常量池中
        System.out.println(a == c);

        // 字符串对象存储在堆中
        String d = new String("悟空2");
        System.out.println(c == d);

        String e = "悟空";
        // f是变量+字面量，在运行期才创建，在堆内存中创建
        String f = e+"2";
        System.out.println(a == f);

        // final修饰的String是一个常量。编译器执行g+2实际上不是变量+字面量，而是用"悟空"替换g，变成了字面量+字面量
        final String g = "悟空";
        String h = g+"2";
        System.out.println(a == h);
    }
}





