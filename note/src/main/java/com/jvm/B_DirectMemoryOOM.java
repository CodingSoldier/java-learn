// package com.jvm;
//
// import sun.misc.Unsafe;
//
// import java.lang.reflect.Field;
//
// /**
//  * VM Args：-Xmx20M -XX:MaxDirectMemorySize=10M
//  * P59
//  *
//  * java com/jvm/B_DirectMemoryOOM
//  */
// public class B_DirectMemoryOOM {
//
//     private static final int _1MB = 1024 * 1024;
//
//
//     public static void main(String[] args) throws Exception {
//         test1();
//     }
//
//     @SuppressWarnings("unchecked")
//     public static void test1() throws Exception {
//         Field unsafeField = Unsafe.class.getDeclaredFields()[0];
//         unsafeField.setAccessible(true);
//         Unsafe unsafe = (Unsafe) unsafeField.get(null);
//         while (true) {
//             unsafe.allocateMemory(_1MB);
//         }
//     }
// }