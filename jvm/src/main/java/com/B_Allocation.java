package com;

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





