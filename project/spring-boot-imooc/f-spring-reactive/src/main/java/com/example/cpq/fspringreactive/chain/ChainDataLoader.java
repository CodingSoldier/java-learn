package com.example.cpq.fspringreactive.chain;

import java.util.concurrent.CompletableFuture;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-07-15
 */
public class ChainDataLoader extends DataLoader{

    public void doLoad() {
        CompletableFuture
                .runAsync(super::loadConfigurations)
                .thenRun(super::loadUsers)
                .thenRun(super::loadOrders)
                .whenComplete((result, throwable) -> { // 完成时回调
                    System.out.println("线程["+Thread.currentThread().getName()+"] 加载完成");
                })
                .exceptionally((throwable) -> {
                    System.out.println("线程["+Thread.currentThread().getName()+"] 加载异常");
                    return null;
                })
                .join(); // 等待完成
    }

    public static void main(String[] args) {
        new ChainDataLoader().load();
        System.out.println("main线程["+Thread.currentThread().getName()+"] 运行");
    }

}
