package com.cpq.bingfa.annoations;

import java.util.concurrent.TimeUnit;

/**
 * @author chenpiqian
 * @date: 2020-10-15
 */
public class TestUnfinishedObj {
    static UnfinishedObj instance;

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            instance = new UnfinishedObj(1, 2);
        }).start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("未初始化完成的实例" + instance);
        TimeUnit.SECONDS.sleep(2);
        System.out.println("已经初始化完成的实例" + instance);
    }
}
