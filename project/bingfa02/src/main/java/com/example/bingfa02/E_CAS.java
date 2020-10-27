package com.example.bingfa02;

public class E_CAS {
}


/**
 CAS：全称 compare and swap。
 CAS有3个操作数，内存值、预期值、更新值。
 当且仅当预期值与内存值相同，才将内存值改为修改值，否则什么都不做，最后返回现在的内存值
 CPU支持比较替换作为一个原子操作
 */

/**
 * 使用代码模拟CAS
 */
class SimulatedCAS{
    private volatile int value;

    public synchronized int compareAndSwap(int expectedValue, int newValue){
        int oldValue = value;
        if (oldValue == expectedValue){
            value = newValue;
        }
        return oldValue;
    }


}

/*
    //sun.misc.Unsafe.getAndAddInt
    CAS加自旋就形成了一个乐观锁
    public final int getAndAddInt(Object var1, long var2, int var4) {
        int var5;
        do {
            var5 = this.getIntVolatile(var1, var2);
        } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

        return var5;
    }

    Unsafe是Java实现CAS的核心类。Java无法直接访问底层操作系统，而是通过Unsafe的本地native方法来访问，不过尽管如此，JVM还是开了一个后门，JDK中有一个类Unsafe，此类提供了硬件级别的原子操作
    valueOffset表示变量值在内存中的偏移地址，因为Unsafe就是根据内存偏移地址获取数据的原值，这样就能通过unsafe实现CAS了

    CAS的问题：
        ABA问题，线程1获取到原值是A，进行一系列操作，线程2修改成B，线程3修改为A，线程1执行到设置值阶段，认为值没有被修改，还是A。
        自旋时间过长。
 */








