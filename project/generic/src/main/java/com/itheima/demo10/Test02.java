package com.itheima.demo10;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @author chenpq05
 * @since 2023/3/23 9:46
 */
public class Test02<T> {

    private T[] arr;

    public Test02(Class<T> clz, int length) {
        this.arr = (T[])Array.newInstance(clz, length);
    }

    public T get(int length) {
        return arr[length];
    }

    public void put(int index, T e) {
        arr[index] = e;
    }

    public T[] getArr() {
        return arr;
    }

    public static void main(String[] args) {
        Test02<String> test02 = new Test02<String>(String.class, 5);
        test02.put(0, "aa0000a");
        test02.put(1, "aaa");
        test02.put(2, "bbb");

        String s = test02.get(2);
        System.out.println(s);

        String[] arr = test02.getArr();
        System.out.println(Arrays.toString(arr));
    }

}
