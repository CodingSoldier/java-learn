package com.datastructure_arithmetic;

import java.util.Random;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-10-12
 */
public class Utils {

    // 生成随机数组
    public static int[] generateIntArray(int n, int randomL, int randomR){
        int[] arr = new int[n];
        for (int i=0; i<arr.length; i++){
            int num = new Random().nextInt(randomR - randomL + 1) + randomL;
            arr[i] = num;
        }
        return arr;
    }

}
