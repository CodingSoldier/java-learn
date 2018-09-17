package com.jvm;


/**
 * VM Args：-Xss2M （这时候不妨设大些）
 * P55
 *
 * 在本目录下编译  javac -encoding utf-8 B_JavaVMStackOOM.java
 * 在src/main/java目录下执行 java -Xss2M com/jvm/B_JavaVMStackOOM
 * 计算机卡死，java程序没有抛出异常
 */
public class B_JavaVMStackOOM {

    private void dontStop() {
        while (true) {
        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontStop();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) throws Throwable {
        B_JavaVMStackOOM oom = new B_JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}