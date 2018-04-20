package com.thinkinginjava;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Author：陈丕迁
 * @Description：
 * @Date： 2018-04-20
 */
public class L_Array {
}


class BerylliumSphere{}
class E01_ArrayInitialization {
    static void hide(BerylliumSphere[] s) {
        System.out.println("Hiding " + s.length + " sphere(s)");
    }
    public static void main(String[] args) {
// Dynamic aggregate initialization:
        hide(new BerylliumSphere[]{ new BerylliumSphere(),
                new BerylliumSphere() });
// The following line produces a compilation error.
// hide({ new BerylliumSphere() });
// Aggregate initialization:
        BerylliumSphere[] d = { new BerylliumSphere(),
                new BerylliumSphere(), new BerylliumSphere() };
        hide(d);
// Dynamic aggregate initialization is redundant
// in the next case:
        BerylliumSphere[] a = new BerylliumSphere[]{
                new BerylliumSphere(), new BerylliumSphere() };
        hide(a);
    }
}





// P450
class E11_AutoboxingWithArrays {
    public static void main(String[] args) {
        int[] pa = { 1, 2, 3, 4, 5 };
        //Integer[] wa = pa;
        Integer[] wb = { 1, 2, 3, 4, 5 };
        //int[] pb = wb;
        //自动包装机制不能应用于数组

        Arrays.sort(wb, Collections.reverseOrder());
    }
}












