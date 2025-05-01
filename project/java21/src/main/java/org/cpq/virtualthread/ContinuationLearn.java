package org.cpq.virtualthread;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class ContinuationLearn {

    public static void main(String[] args) throws Exception {
        // Continuation continuation = getContinuation01();
        // continuation.run();

        Continuation continuation = getContinuation02();
        continuation.run();
        System.out.println("do some thing after yield A");
        continuation.run();
        System.out.println("do some thing after yield B");
        continuation.run();

        boolean done = continuation.isDone();
        if (done){
            System.out.println("continuation terminated , you can not run continuation.run()");
        }
    }


    /**
     * Continuation是java底层内部api对象，
     * 1、需要在idea配置 -> java Compiler -> xxx line parameters 填写：
     *      --enable-preview --add-exports java.base/jdk.internal.vm=ALL-UNNAMED
     * 2、还需要在idea本类配置VM参数
     *      --enable-preview
           --add-exports java.base/jdk.internal.vm=ALL-UNNAMED
     * 才能使用
     */
    private static Continuation getContinuation01() {
        var scope = new ContinuationScope("test");
        /**
         * Runnable被包装在Continuation中，但Continuation不是线程，用Continuation运行线程，更加节省内存
         */
        var continuation = new Continuation(scope, () -> {
            System.out.println("A");
            System.out.println("B");
            System.out.println("C");
        });
        return continuation;
    }

    private static Continuation getContinuation02() {
        var scope = new ContinuationScope("test");
        var continuation = new Continuation(scope, () -> {
            System.out.println("A");
            // 暂停Continuation的Runnable。程序控制权将返回到调用continuation.run()的线程。。
            Continuation.yield(scope);
            System.out.println("B");
            Continuation.yield(scope);
            System.out.println("C");
        });
        return continuation;
    }
}
