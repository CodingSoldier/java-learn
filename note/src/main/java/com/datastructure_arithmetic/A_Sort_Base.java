package com.datastructure_arithmetic;

import org.junit.Test;

import java.util.Date;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-10-12
 */
public class A_Sort_Base {

    // 选择排序
    public static void selectionSort(int arr[]){
        for (int i=0; i<arr.length; i++){
            int minIndex = i;
            for (int j=i+1; j<arr.length; j++){
                if (arr[minIndex]<arr[j]){
                    minIndex = j;
                }
            }

            //互换元素
            int iNum = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = iNum;
        }
    }
    @Test
    public void test_selectionSort(){
        //int[] arr = {10,9,8,7,6,5,4,3,2,1};
        int[] arr = Utils.generateIntArray(1000, 0, 1000);
        selectionSort(arr);
        for (int i=0; i<arr.length; i++){
            System.out.println(arr[i]);
        }
    }


    /**
     * 插入排序
     * 插入排序相比选择排序的优势是可以提前终止内层循环，即插入排序的循环次数更少
     * 但由于插入排序的交换操作是在内层循环中，这很耗时，所有下面的插入排序比选择排序可能更加耗时
     */
    public static void insertSort(int[] arr){
        for (int i=0; i<arr.length; i++){
            // 前面已经排序的元素多加一个元素之后，再从尾向头遍历
            for (int j=i; j>=1; j--){
                //发现新增的最后一个元素比前面的元素小，最后一个元素需要跟前面的元素比较、交换
                if (arr[j] < arr[j-1]){
                    int temp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = temp;
                }else{
                    break;
                }
            }

            // 优化代码
            for (int j=i; j>=1 && arr[j] < arr[j-1]; j--){
                int temp = arr[j];
                arr[j] = arr[j-1];
                arr[j-1] = temp;

            }
        }
    }

    /**
     * 优化插入排序，不再内层循环中创建元素
     * java似乎是创建元素更加耗时，所以insertSort比insertSortChangeOnce慢
     * insertSortChangeOnce没在内层循环中创建元素
     */
    public static void insertSortOnce(int[] arr){
        for (int i=0; i<arr.length; i++){
            int e = arr[i];
            int j;
            // 前面已经排序的元素比当前元素e大，需要交换位置
            for (j=i; j>=1 && arr[j-1] > e; j--){
                // 前面的元素向后移动一个位置
                arr[j] = arr[j-1];
            }
            // 前面的元素不大于e了，则当前位置赋值为e
            // arr[j]的原始值已经移动到arr[j-1]了
            arr[j]=e;
        }
    }
    @Test
    public void test_insertSort(){
        int[] arr1 = Utils.generateIntArray(100000, 0, 10);
        int[] arr2 = Utils.generateIntArray(100000, 0, 10);
        int[] arr3 = Utils.generateIntArray(100000, 0, 10);

        long time1 = new Date().getTime();
        selectionSort(arr1);
        System.out.println(new Date().getTime() - time1);

        long time2 = new Date().getTime();
        insertSort(arr2);
        System.out.println(new Date().getTime() - time2);

        long time3 = new Date().getTime();
        insertSortOnce(arr3);
        System.out.println(new Date().getTime() - time3);

        //for (int i=0; i<arr1.length; i++){
        //    System.out.println(arr1[i]);
        //}
    }

}
