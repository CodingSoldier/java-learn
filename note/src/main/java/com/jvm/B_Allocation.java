package com.jvm;


import java.util.ArrayList;
import java.util.List;


/**
 * P51
 * VM Args：-Xms20m -Xmx20m -XX:+HeapDumpOnOutOfMemoryError
   1、堆储存快照dump到了java-learn目录，后缀为 .hprof
   2、使用jvisualvm.exe打开
   3、B_Allocation_HeapOOM内存溢出.jpg 可以看到com.jvm.HeapOOM.OOMObject这个类的实例太多了
   4、.hprof可能很大

    java默认最大堆是物理内存的 1/4
    com.demo.jvm.OutOfMemoryDemo  linux jar测试
 */
class HeapOOM {

    static class OOMObject {
    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<OOMObject>();

        while (true) {
            list.add(new OOMObject());
        }
    }
}



/**
 * VM Args：-Xss128k
 * P53
 */
class JavaVMStackSOF {

    private int stackLength = 1;

    public void stackLeak() {
        stackLength++;
        stackLeak();
    }

    public static void main(String[] args) throws Throwable {
        JavaVMStackSOF oom = new JavaVMStackSOF();
        try {
            oom.stackLeak();
        } catch (Throwable e) {
            System.out.println("stack length:" + oom.stackLength);
            throw e;
        }
    }
}


/**
 *  jar -Xms512m -Xmx512m -XX:PermSize=512M -XX:MaxPermSize=1024m -Djava.awt.headless=true
 linux执行  java -XX:+PrintFlagsFinal -version | grep :

 UseCompressedOops true 启动指针压缩，当服务器内存在4-32G时有效，所以说java服务器内存最好不要大于32G
 UseParallelGC  true  新生代使用Parallel Scavenge 老年代使用Serial Old（PS MarkSweep）

 */

public class B_Allocation {
    private static final int _1MB = 1024 * 1024;
    //P92
    // -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8
    public static void main(String[] args){
        byte[] a1, a2, a3, a4;
        a1 = new byte[2 * _1MB];
        a2 = new byte[2 * _1MB];
        a3 = new byte[2 * _1MB];
        a4 = new byte[4 * _1MB];
    }

}


class B_Allocation2 {
    private static final int _1MB = 1024 * 1024;
    //P95
    // -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=1 -XX:+PrintTenuringDistribution
    public static void main(String[] args){
        byte[] a1, a2, a3, a4;
        a1 = new byte[_1MB / 4];
        a2 = new byte[4 * _1MB];
        a3 = new byte[4 * _1MB];
        a3 = null;
        a4 = new byte[4 * _1MB];
    }

}

class B_Allocation3 {
    private static final int _1MB = 1024 * 1024;
    // P97没效果
    // -verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:+PrintGCDetails -XX:SurvivorRatio=8 -XX:MaxTenuringThreshold=15 -XX:+PrintTenuringDistribution
    public static void main(String[] args){
        byte[] a1, a2, a3, a4;
        a1 = new byte[_1MB / 4];
        a2 = new byte[_1MB / 4];
        a3 = new byte[4 * _1MB];
        a4 = new byte[4 * _1MB];
        a4 = null;
        a4 = new byte[4 * _1MB];
    }

}



class TTT{
    static class OOMObject{
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillOutOfMemory(int num){
        List<OOMObject> list = new ArrayList<>();
        for (int i = 0; i < num ; i++){
            list.add(new OOMObject());
        }
    }

    public static void main(String[] args){
        boolean a = true;
        while (a){
            fillOutOfMemory(1000);
        }
    }

}





