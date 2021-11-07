package com.datastructure_arithmetic_2;

public class Util {

    private Util(){}

    public static Integer[] generateRandomArray(int n, int rangeL, int rangeR) {

        assert n > 0 && rangeL <= rangeR;

        Integer[] arr = new Integer[n];
        for (int i = 0; i < n; i++)
            arr[i] = (int)(Math.random() * (rangeR - rangeL + 1)) + rangeL;
        return arr;
    }

    public static Integer[] generateOrderedArray(int n) {
        assert n > 0;

        Integer[] arr = new Integer[n];

        for (int i = 0; i < n; i++)
            arr[i] = i;
        return arr;
    }

    public static void main(String[] args) {
        // Integer i1 = 1595008451;
        // String s = Integer.toBinaryString(i1);
        // System.out.println(s);
        //
        // int i2 = i1 >>> 1;
        // String s2 = Integer.toBinaryString(i2);
        // System.out.println(s2);

        int i2 = 1<<5;
        String s2 = Integer.toBinaryString(i2);
        System.out.println(s2);
    }

}
