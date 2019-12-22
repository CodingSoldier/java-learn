package com.example.redis2;

import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class Redisson02 {

    @Autowired
    RedissonClient redissonClient;

    @Test
    void test2() {
        RLock rLock = redissonClient.getLock("redisson-lock");
        boolean getRlock = false;
        try {
            if (getRlock = rLock.tryLock(0, 40, TimeUnit.SECONDS)){
                System.out.println("获取到锁");
            }else {
                System.out.println("没没没获取到锁");
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            if (!getRlock){
             return;
            }
            // 没获取到锁，RLock不是null，但是调用unlock会抛异常
            rLock.unlock();
        }
    }



}
