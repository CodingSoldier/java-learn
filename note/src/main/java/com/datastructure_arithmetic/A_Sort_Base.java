package com.datastructure_arithmetic;

import org.junit.Test;

import java.util.Date;
import java.util.Random;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-10-12
 */
public class A_Sort_Base<T> {

    // 选择排序
    public static void selectionSort(int arr[]) {
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;

            //找出未排序元素最小值的下标
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }

            //未排序元素最小值交换到未排序元素的最前面
            int iNum = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = iNum;
        }
    }


    /**
     * 生成随机数组
     * @param n 数组长度
     * @param randomL  数组最小元素
     * @param randomR  数组最大元素
     */
    public static int[] generateIntArray(int n, int randomL, int randomR){
        int[] arr = new int[n];
        for (int i=0; i<arr.length; i++){
            int num = new Random().nextInt(randomR - randomL + 1) + randomL;
            arr[i] = num;
        }
        return arr;
    }

    @Test
    public void test_selectionSort() {
        int[] arr = generateIntArray(1000, 0, 1000);
        selectionSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    /**
     * 插入排序
     * 插入排序相比选择排序的优势是可以提前终止内层循环，即插入排序的循环次数更少
     * 但由于插入排序的交换操作是在内层循环中，这很耗时，所有下面的插入排序可能比选择排序更加耗时
     */
    public static void insertSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {

            //// 前面已经排序的元素多加一个元素之后，将此元素与前面已经排序的元素比较
            //for (int j = i+1; j >= 1; j--) {
            //    //新增的最后一个元素比前面的元素小，交换位置
            //    if (arr[j] < arr[j - 1]) {
            //        int temp = arr[j];
            //        arr[j] = arr[j - 1];
            //        arr[j - 1] = temp;
            //    } else {
            //        break;
            //    }
            //}

            // 上面的代码可以这样优化，减少内层循环次数
            for (int j = i; j >= 1 && arr[j] < arr[j - 1]; j--) {
                int temp = arr[j];
                arr[j] = arr[j - 1];
                arr[j - 1] = temp;
            }
        }
    }

    @Test
    public void test_insertSort() {
        int[] arr = generateIntArray(1000, 0, 1000);
        insertSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    /**
     * 优化插入排序，不在内层循环中创建元素
     * java似乎是创建元素更加耗时，所以insertSort比insertSortOnce慢
     * insertSortOnce没在内层循环中创建元素
     */
    public static void insertSortNotNew(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            int e = arr[i];
            int j;
            // 当前元素e小于前面已经排序的元素，需要交换位置
            for (j = i; j >= 1 && e < arr[j - 1] ; j--) {
                // 前面的元素向后移动一个位置
                arr[j] = arr[j - 1];
            }
            // 前面的元素不大于e了，则当前位置赋值为e
            // arr[j]的原始值已经移动到arr[j-1]了
            arr[j] = e;
        }
    }

    @Test
    public void test_insertSortNotNew() {
        int[] arr = generateIntArray(1000, 0, 1000);
        insertSortNotNew(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    // 测试选择排序、插入排序性能
    @Test
    public void test_time01() {
        int[] arr1 = generateIntArray(100000, 0, 100000);
        int[] arr2 = generateIntArray(100000, 0, 100000);
        int[] arr3 = generateIntArray(100000, 0, 100000);

        long time1 = new Date().getTime();
        selectionSort(arr1);
        System.out.println(new Date().getTime() - time1);

        long time2 = new Date().getTime();
        insertSort(arr2);
        System.out.println(new Date().getTime() - time2);

        long time3 = new Date().getTime();
        insertSortNotNew(arr3);
        System.out.println(new Date().getTime() - time3);

    }











    public void mergeSort(int[] arr){
        mergeSegment(arr, 0, arr.length-1);
    }

    public void mergeSegment(int[] arr, int minIndex, int maxIndex){
        if (minIndex >= maxIndex){
            return;
        }

        int middleIndex = (minIndex + maxIndex)/2;
        mergeSegment(arr, minIndex, middleIndex);
        mergeSegment(arr, middleIndex+1, maxIndex);

        segmentMergeSort(arr, minIndex, middleIndex, maxIndex);
    }

    public void segmentMergeSort(int[] arr, int minIndex, int middleIndex, int maxIndex){
        int[] segmentArr = new int[maxIndex - minIndex + 1];
        for (int i=minIndex; i<=maxIndex; i++){
            segmentArr[i-minIndex] = arr[i];
        }

        int leftSegmentIndex=minIndex, rightSegmentIndex=middleIndex+1;

        //遍历segmentArr
        for (int current=minIndex; current<=maxIndex; current++){
            if (leftSegmentIndex > middleIndex){
                arr[current] = segmentArr[rightSegmentIndex - minIndex];
                rightSegmentIndex++;
            }else if (rightSegmentIndex > maxIndex){
                arr[current] = segmentArr[leftSegmentIndex - minIndex];
                leftSegmentIndex++;
            }else if (segmentArr[leftSegmentIndex - minIndex] < segmentArr[rightSegmentIndex - minIndex]){
                arr[current] = segmentArr[leftSegmentIndex - minIndex];
                leftSegmentIndex++;
            }else {
                arr[current] = segmentArr[rightSegmentIndex - minIndex];
                rightSegmentIndex++;
            }
        }

    }


    @Test
    public void test_mergeSort() {
        int[] arr1 = generateIntArray(100000, 0, 100000);

        long time1 = new Date().getTime();
        mergeSort(arr1);
        System.out.println("**消耗时间**"+(new Date().getTime() - time1));

        for (int i=0; i<arr1.length; i++){
            System.out.println(arr1[i]);
        }
    }



}
