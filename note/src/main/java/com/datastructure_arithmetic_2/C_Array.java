package com.datastructure_arithmetic_2;

import org.junit.Test;

public class C_Array {

    /*
    二分查找法的思想在1946年提出
    但是第一个没有bug的二分查找法在1962年才出现
     */

    /**
     * 使用二分查找法查找有序数组中的元素
     *
     * @param arr    数组
     * @param n      元素个数
     * @param target 查找的元素
     * @return 数组下标
     *
     * 对于一个算法来说，往往会在边界问题上出错
     */
    public static int binarySearch(Comparable[] arr, int n, Comparable target) {
        /**
         * 定义左边界、右边界。则查找范围便确定了，是[l, r]，左右边界都是闭区间。
         * [l, r]也称为循环不变量，后面改变边界值l、r，依旧要保持循环不变量[l, r]有效
         */
        int l = 0, r = n - 1;
        // l <= r 对于循环不变量[l, r]依旧有效，即l==r依旧有效
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (target.compareTo(arr[mid]) == 0) {
                return mid;
            }
            /**
             * target比中间的值小，数据在左边
             * r = mid - 1 能以最少的数据量保持[l, r]有效
             * 如果是 r = mid ，target一定是小于r的，判断条件都给出了这个结论。没必要多加一个数据进去
             */
            if (target.compareTo(arr[mid]) < 0) {
                r = mid - 1;  // target在[l, mid - 1]中，仍然是左右为闭区间的循环不变体
            } else {
                l = mid + 1;  // target在[mid + 1, r]中，仍然是左右为闭区间的循环不变体
            }
        }
        return -1;
    }

    /**
     * 修改了循环不变量的定义 [l, r)
     * 边界值l、r随之改变
     */
    public static int binarySearch2(Comparable[] arr, int n, Comparable target){
        int l = 0, r =n; // [l, r)
        while (l < r){
            int mid = l + (r -l)/2;
            if (target.compareTo(arr[mid]) == 0){
                return mid;
            }
            if (target.compareTo(arr[mid]) < 0){
                r = mid;  // [l, mid)
            }else {
                l = mid + 1;  // [mid+1, r]
            }
        }
        return -1;
    }

    @Test
    public void test_binarySearch() {
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        //int index = binarySearch(arr, 10, 10);

        int index = binarySearch2(arr, 10, 1);
        System.out.println(index);
    }


}
