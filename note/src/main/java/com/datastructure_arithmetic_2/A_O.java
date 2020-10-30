package com.datastructure_arithmetic_2;


public class A_O {
    /*
    什么是大O
        n表示数据规模
        O(f(n))表示运行算法所需要执行的指令数，和f(n)成正比
        严格来说，我们使用O来表示算法执行的最低上界

        二分查找法 O(logn)             所需执行指令数  a*logn
        寻找数组中的最大/最下值 O(n)     所需执行指令数  b*n
        归并排序算法 O(nlogn)           所需执行指令数  c*nlogn
        选择排序法 O(n^2)              所需执行指令数  d*n^2

        时间复杂度一般用来讨论大数据量，所以常数项a、b、c、d可以忽略，只讨论n

     */


    public static void selectorSort(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.println(i+"次排序前"+getArrStr(arr));
            int minIndex = i;
            for (int j = i+1; j < arr.length; j++) {
                if (arr[j] < arr[minIndex]){
                    minIndex = j;
                }
            }

            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;

            System.out.println(i+"次排序后"+getArrStr(arr));
        }
    }

    public static void insertSort(int[] arr){

        //for (int i = 0; i < arr.length; i++) {
        //    System.out.println(i+"次排序前"+getArrStr(arr));
        //    for (int j = i; j >= 1; j--) {
        //        if (arr[j] < arr[j-1]){
        //            int temp = arr[j];
        //            arr[j] = arr[j-1];
        //            arr[j-1] = temp;
        //        }else {
        //            break;
        //        }
        //    }
        //    System.out.println(i+"次排序后"+getArrStr(arr));
        //}

        for (int i = 0; i < arr.length; i++) {
            for (int j = i; j >= 1 && arr[j] < arr[j-1]; j--) {
                int temp = arr[j];
                arr[j] = arr[j-1];
                arr[j-1] = temp;
            }
        }

    }

    public static void main(String[] args) {
        int[] arr = {4,5,3,1,6,2};
        //selectorSort(arr);

        insertSort(arr);

        System.out.println( getArrStr(arr) );
    }

    public static String getArrStr(int[] arr){
        String str = "[";
        for (int i = 0; i < arr.length; i++) {
            str += arr[i];
            if (i != arr.length -1){
                str += ", ";
            }
        }
        str += "]";
        return str;
    }
}
