package org.cpqjvm.g1;

public class Demo01 {

    /**
     * G1最大的特征：将堆分为若干小区域，每个小区域称为region
     * 如图片：G1垃圾收集与传统垃圾收集对比.jpg
     *
     * G1对象管理：
     *      新生代对象分配在新生代Eden区
     *      运行一段时间后启动YoungGC，将Eden区中的对象复制到Survivor区
     *      继续运行一段时间后Eden区再次被填满，Eden区和Survivor区对象复制到一个新的Survivor区
     *          如果老的Survivor区的对象复制次数超过规定值，直接将对象复制到Old区
     *      如图片：G1对象管理过程.jpg
     *      如果一个对象大于等于分区的一半，则直接将对象放到H区（Humongous区）
     *      Eden区、Survivor区、Old区、H区的内存地址是动态变化的，一个region为Eden区，Eden区清空后，后面可能会变成Survivor区、Old区、H区
     *
     */

    /**
     * 配置VM参数再运行 -Xmx128M -XX:+UseG1GC -Xlog:gc*
     * 最后输出 region size 1024K，说明G1GC默认的region大小为1024K
     */
    // public static void main(String[] args) {
    //     byte[] data = new byte[1024 * 256];
    //     for (int i = 0; i < 1000; i++) {
    //         data = new byte[1024 * 256];
    //     }
    //
    // }

    // /**
    //  * region的个数和大小都是动态可变的
    //  * G1默认region最大个数为2048个，region大小的取值范围是2的指数倍：1、2、4、8、16、32M
    //  * region最大值 = max( (Xmx + Xms) 除以2再除以2048 , 1MB)
    //  * 配置-Xmx4096M -Xms4096M，通过公式得出region size为2048K
    //  * 配置VM参数再运行main方法 -Xmx4096M -Xms4096M -XX:+UseG1GC -Xlog:gc*
    //  * 最后输出 region size 2048K
    //  *
    //  * 在启动时设置region的大小会影响JVM调优，
    //  * 不建议手动指定region的大小，而应该通过调整Xmx和Xms让JVM自动调整region的大小
    //  *
    //  * G1只有YoungGC、MixedGC和FullGC
    //  * YoungGC：只回收新生代区域，代价低/频率高
    //  * MixGC：回收全部新生代 + 部分老年代，频率一般
    //  * FullGC：回收全部堆空间，代价高/频率低
    //  */
    // public static void main(String[] args) {
    //     byte[] data = new byte[1024 * 256];
    //     for (int i = 0; i < 1000; i++) {
    //         data = new byte[1024 * 256];
    //     }
    //
    // }

    /**
     * YGC的基本过程：
     *    1、从GC roots出发标记存活对象。GC roots有哪些：栈对象、静态变量、运行时常量池、程序计数器、锁
     *    2、复制对象到S区，复制过程是最慢的
     *    3、释放垃圾集合，回收region
     *    4、动态调整（增加或减少）新生代region数量
     *    4、判断是否开启并发标记，为下一步执行混合回收做准备。如果堆空间使用率超过45%则会进行混合回收
     *
     * Eden区、Survivor区采用标记-复制算法。标记和复制过程是同时进行的，找到一个存活对象就复制一个
     *
     * -XX:MaxGCPauseMillis 用于配置GC停顿时间
     * -Xmx128M -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -Xlog:gc*
     */
    // public static void main(String[] args) {
    //     byte[] data = new byte[1024 * 256];
    //     for (int i = 0; i < 100; i++) {
    //         data = new byte[1024 * 256];
    //     }
    //
    // }

    /**
     * 默认情况下，YGC之后，已分配内存超过内存总容量的45%会触发混合回收MixGC
     * 混合回收基本步骤：
     *    第一步：初始标记阶段。标记所有由GCRoot等直接引用的对象，会暂停用户程序的运行
     *    第二步：并发标记。标记上一步中标记的所有引用对象，执行时间略长，不会暂停用户程序的运行，不会STW
     *    第三步：再标记阶段。标记出上一个阶段没有被标记的对象，执行速度非常快，会STW
     *    第四步：存活对象计数阶段。统计出每个region存活对象的数量
     *    第五步：垃圾回收阶段。选择回收价值高的区域，把存活对象复制到新分区，然后回收老分区
     *
     * 图片：G1混合回收完整过程.jpg
     *
     * YGC是MixedGC的前奏，YGC完成，就代表mixedGC已经走完了初始标记阶段，YGC已经帮MixedGC完成了初始化的活
     */
    // 触发混合回收
    // public static List list = new ArrayList<>();
    // public static void main(String[] args) throws Exception{
    //     while (true) {
    //         byte[] data = new byte[1024 * 256];
    //         for (int i = 0; i < 50; i++) {
    //             data = new byte[1024 * 256];
    //             list.add(data);
    //         }
    //         TimeUnit.SECONDS.sleep(1L);
    //     }
    // }

    /**
     * 老年代的空间已经不足以放下新对象，会触发FullGC
     * FullGC可能执行两次，第二次是回收软引用，如果堆空间还是不足以存放新对象，会触发OOM
     * YGC、MixedGC采用标记-复制算法。
     * FullGC采用标记-压缩算法，FullGC是没有堆空间可用了才触发的，因为没有可用的region了，就不能采用复制算法了
     */


}
