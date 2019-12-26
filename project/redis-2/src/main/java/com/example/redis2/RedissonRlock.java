package com.example.redis2;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
class RedissonRlock {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    RedissonClient redissonClient;

    ///**
    // * 分布式锁
    // * @throws Exception
    // */
    ////@Scheduled(cron="0 */1 * * * ?")
    //@Scheduled(cron="*/10 * * * * ?")
    //public void test0() throws Exception{
    //    long timeoutMillis = 12000L;
    //    String LOCK_KEY = "distributed-key";
    //    ValueOperations valueOps = redisTemplate.opsForValue();
    //
    //    /**
    //     * 锁的值是时间戳加锁超时时间
    //     * setIfAbsent()加时间也是原子性操作  https://redis.io/commands/set
    //     */
    //    Boolean lock = valueOps.setIfAbsent(LOCK_KEY, String.valueOf(System.currentTimeMillis() + timeoutMillis));
    //    // 设置过期时间
    //    redisTemplate.expire(LOCK_KEY, timeoutMillis, TimeUnit.MILLISECONDS);
    //
    //    if (lock != null && lock){
    //        /**
    //         * 要睡眠一点时间，若线程1执行 valueOps.setIfAbsent() 到delete(key)时间极短；线程2此时才开始执行valueOps.setIfAbsent()任然能获取到锁
    //         * 即获取锁到删除锁的时间间隔还是要
    //         */
    //        TimeUnit.MILLISECONDS.sleep(100);
    //        // 获取到锁，执行定时任务
    //        System.out.println(new Date() + "获取到锁lock");
    //        // 定时任务执行完毕，删除锁
    //        redisTemplate.delete(LOCK_KEY);
    //    }else {
    //        // 获取锁的原始值，也可以把值当做锁的有效期
    //        Object obj = valueOps.get(LOCK_KEY);
    //        long oldVal = obj == null ? -1 : Long.parseLong(obj.toString());
    //        if (System.currentTimeMillis() > oldVal){
    //            // 当前时间大于锁有效期，则锁无效
    //
    //            // 使用原子方法getSet()给锁设置一个新的有效期，并返回锁（旧有效期）
    //            Object oldValThread = valueOps.getAndSet(LOCK_KEY, String.valueOf(System.currentTimeMillis() + timeoutMillis));
    //            redisTemplate.expire(LOCK_KEY, timeoutMillis, TimeUnit.MILLISECONDS);
    //
    //            /**
    //             * 锁为空，可执行
    //             * oldValThread == oldVal 是为了保证多线程执行时，只有当前线程通过getSet()方法获取的值与锁的原始值相等，才判定当前线程获取到锁
    //             */
    //            if (oldValThread == null || Long.parseLong(oldValThread.toString()) == oldVal){
    //                TimeUnit.MILLISECONDS.sleep(100);
    //                System.out.println(new Date() + "获取到锁");
    //                redisTemplate.delete(LOCK_KEY);
    //            }
    //        }
    //    }
    //}

    //@Scheduled(cron="0 */1 * * * ?")
    //void test1() {
    //    RLock rLock = redissonClient.getLock("redisson-lock");
    //    boolean getRlock = false;
    //    try {
    //        /**
    //         * 因为最后悔释放lock，
    //         * 当定时任务时间执行时间小于waittime，同样会导致定时任务执行多次，
    //         * 所以如果不知道waitTime是多少，就设置为0
    //         */
    //        if (getRlock = rLock.tryLock(0, 50, TimeUnit.SECONDS)){
    //            System.out.println("redisson获取到锁");
    //        }else {
    //            System.out.println("redisson没没没获取到锁");
    //        }
    //    }catch (InterruptedException e){
    //        e.printStackTrace();
    //    }finally {
    //        if (!getRlock){
    //         return;
    //        }
    //        // 没获取到锁，RLock不是null，但是调用unlock会抛异常
    //        rLock.unlock();
    //    }
    //}


    //@Scheduled(cron="10 */1 * * * ?")
    //void test2() {
    //    /**
    //     * setIfAbsent使用的是  SET key value EX 时间秒 NX 。文档地址 https://redis.io/commands/set
    //     * 是原子性操作
    //     */
    //    Boolean b = RedisUtil.setIfAbsent("redis-lock-key", "value", Duration.ofSeconds(20));
    //    if (b !=null && b){
    //        System.out.println("####redis获取到锁");
    //    }else {
    //        System.out.println("####redis没没没获取到锁");
    //    }
    //}


}
