package com.example.interview;

/**
 Java对象头和Monitor是实现synchronized的基础
 java对象头：
 Mark Word 默认存储对象的hashCode、分代年龄、锁类型、锁标志位等信息
 无锁状态 锁标志位01
 轻量级锁 锁标志位00
 重量级锁 锁标志位10
 偏向锁   锁标志位01 还包含是否偏向锁标志位1
 Class Metadata Address 类型指针指向对象的类元数据，JVM通过这个指针确定该对象是哪个类的数据
 Monitor：每个java对象天生自带一把看不见的锁
 */
public class E1_SyncBlockAndMethod {

    public void syncsTask(){
        synchronized (this){
            System.out.println("同步代码块");
        }
    }

    public synchronized void syncTask(){
        System.out.println("方法同步代码");
    }



/**
 字节码层面理解synchronized

 public void syncsTask();
 descriptor: ()V
 flags: ACC_PUBLIC
 Code:
 stack=2, locals=3, args_size=1
 0: aload_0
 1: dup
 2: astore_1
 3: monitorenter          //## monitorenter指令进入同步代码块，锁计数器加1
 4: getstatic     #2
 7: ldc           #3
 9: invokevirtual #4
 12: aload_1
 13: monitorexit         //## monitorexit指令退出同步代码块，锁计数器减1
 14: goto          22
 17: astore_2
 18: aload_1
 19: monitorexit        //## 一个monitorenter对应两个monitorexit是因为最后这个monitorexit用于发生异常时释放锁
 20: aload_2
 21: athrow
 22: return
 Exception table:
 from    to  target type
 4    14    17   any
 17    20    17   any
 LineNumberTable:
 line 10: 0
 line 11: 4
 line 12: 12
 line 13: 22
 LocalVariableTable:
 Start  Length  Slot  Name   Signature
 0      23     0  this   Lcom/example/interview/E1_SyncBlockAndMethod;
 StackMapTable: number_of_entries = 2
 frame_type = 255 full_frame
    offset_delta = 17
    locals = [ class com/example/interview/E1_SyncBlockAndMethod, class java/lang/Object ]
    stack = [ class java/lang/Throwable ]
    frame_type = 250
    offset_delta = 4

    public synchronized void syncTask();   //## 同步方法是隐式调用monitorenter、monitorexit，在字节码中没有展示
    descriptor: ()V
    flags: ACC_PUBLIC, ACC_SYNCHRONIZED
    Code:
    stack=2, locals=1, args_size=1
            0: getstatic     #2
            3: ldc           #5
            5: invokevirtual #4
            8: return
    LineNumberTable:
    line 16: 0
    line 17: 8
    LocalVariableTable:
    Start  Length  Slot  Name   Signature
            0       9     0  this   Lcom/example/interview/E1_SyncBlockAndMethod;
*/


/**
 * 从互斥锁的设计上来看，当一个线程试图操作一个有其他线程持有的对象锁临界资源时，将会处于阻塞状态
 * 但当一个线程再次请求自己持有对象锁的临界资源时，这种情况属于重入
 */

/**
 * 自旋锁
 *    多数情况下，共享数据的锁定状态持续时间较短，这种情况下切换线程不值得。
 *    避免切换线程的方式是让线程执行忙循环等待锁的释放，不让出cpu，这种忙循环就叫自旋
 *
 * 自适应自旋锁
 *    自旋的次数不再固定，由前一次在同一个锁上的自旋时间及锁的拥有者状态来决定。
 *    假如上一次自旋一定时间后获取到了锁，则本次也会选择自旋；若前面多次自旋也没获取到锁，则本次会放弃自旋过程
 *
 * 锁消除
 *    JIT编译时，对运行上下文进行扫描，去除不可能存在竞争的锁
 *
 * 锁粗化
 *    比如加锁操作在循环中，就将加锁粗化到循环外
 *
 * synchronized的4种状态
 *    无锁、偏向锁、轻量级锁、重量级锁
 * synchronized锁会根据情况膨胀也会降级
 *
 * 偏向锁：
 *    如果一个线程获得了锁，那么锁就进入偏向模式，此时Mark Work的结构变为偏向锁结构。
 *    当该线程再次请求锁时，无需再次做任何同步操作
 *    即获取锁的过程只需要检查Mark Work的锁标记位是否为偏向锁，检查当前线程ID是否等会Mark Word的ThreadID即可
 *    这样就省去了大量有关锁申请的操作
 *    适用于重入锁情况
 *
 * 轻量级锁
 *    由偏向锁升级来的，偏向锁运行在一个线程进入同步代码快的情况下
 *    当第二个线程加入锁竞争的时候，偏向锁会升级为轻量级锁
 *    适用于线程交替执行同步代码块的场景
 *    若同一个时间多个线程访问同一个锁，轻量级锁会膨胀为重量级锁
 *
 * 锁的内存语义
 *    线程释放锁，java内存模型会把该线程本地内存的共享变量刷新到主内存中
 *    线程获取锁，java内存模型会把该线程本地内存的共享变量置为无效，从而使得监视器保护的临界区代码必须从主内存中读取共享变量
 *
 *
 *
 * volatile变量为何立即可见
 *    写volatile变量时，JMM会把该线程工作内存中的共享变量立刻刷新到主内存中
 *    读volatile变量时，JMM会把该线程工作内存中的共享变量先置为无效，再到主内存读取
 *
 */

}
