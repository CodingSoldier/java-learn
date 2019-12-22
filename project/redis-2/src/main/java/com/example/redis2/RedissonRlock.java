package com.example.redis2;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@EnableScheduling
class RedissonRlock {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;

    @Scheduled(cron="0 */1 * * * ?")
    void test1() {
        RLock rLock = redissonClient.getLock("redisson-lock");
        boolean getRlock = false;
        try {
            /**
             * 因为最后悔释放lock，
             * 当定时任务时间执行时间小于waittime，同样会导致定时任务执行多次，
             * 所以如果不知道waitTime是多少，就设置为0
             */
            if (getRlock = rLock.tryLock(0, 50, TimeUnit.SECONDS)){
                System.out.println("redisson获取到锁");
            }else {
                System.out.println("redisson没没没获取到锁");
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

    @Scheduled(cron="10 */1 * * * ?")
    void test2() {
        /**
         * setIfAbsent使用pipeline一次发送多条指令
         * 但是在redis服务端，pipeline中的指令仍然是一条一条的执行，所以当redis执行setNx后宕机，任然会有死锁的可能
         */
        Boolean b = RedisUtil.setIfAbsent("redis-lock-key", "value", Duration.ofSeconds(20));
        if (b !=null && b){
            System.out.println("####redis获取到锁");
        }else {
            System.out.println("####redis没没没获取到锁");
        }
    }


}
