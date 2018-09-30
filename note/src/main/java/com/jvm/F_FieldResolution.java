package com.jvm;

public class F_FieldResolution {
    interface Interface0 {
        int A = 0;
    }

    interface Interface1 extends Interface0 {
        int A = 1;
    }

    interface Interface2 {
        int A = 2;
    }

    static class Parent implements Interface1 {
        public static int A = 3;
    }

    static class Sub extends Parent implements Interface2 {
        public static int A = 4;  //注释这行，无法确定Sub.A是3还是2
    }

    public static void main(String[] args) {
        System.out.println(Sub.A);
    }
}
