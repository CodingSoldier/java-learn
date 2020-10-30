package com.datastructure_arithmetic;

import org.junit.Test;

import java.util.Date;
import java.util.Random;

/**
 * @Description
 * @Author chenpiqian
 * @Date: 2019-10-12
 */
public class A_Arithmetic<T> {

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

            // 前面已经排序的元素多加一个元素之后，将此元素与前面已经排序的元素比较
            for (int j = i; j >= 1; j--) {
                //新增的最后一个元素比前面的元素小，交换位置
                if (arr[j] < arr[j - 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = temp;
                } else {
                    break;
                }
            }

            // 上面的代码可以这样优化，减少内层循环次数
            //for (int j = i; j >= 1 && arr[j] < arr[j - 1]; j--) {
            //    int temp = arr[j];
            //    arr[j] = arr[j - 1];
            //    arr[j - 1] = temp;
            //}
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









    // 归并排序
    public void mergeSort(int[] arr){
        mergeSegment(arr, 0, arr.length-1);
    }

    /**
     * 归并排序私有方法
     * @param arr
     * @param minIndex 最小下标
     * @param maxIndex 最大下标
     */
    private void mergeSegment(int[] arr, int minIndex, int maxIndex){

        //if (minIndex >= maxIndex){
        //    return;
        //}

        // 优化1
        // 当需要排序的数据量很小的时候，使用插入排序提高效率
        // 这种优化适用于很多的高级算法
        // 插入排序请看这篇博客 https://mp.csdn.net/postedit/102546480
        if (maxIndex - minIndex <= 15){
            for (int i = minIndex; i<=maxIndex; i++){
                int e = arr[i];
                int j;
                for (j=i; j-1>=minIndex && arr[j-1] > e; j--){
                    arr[j] = arr[j-1];
                }
                arr[j] = e;
            }
            return;
        }

        int middleIndex = (minIndex+maxIndex)/2;
        // 数组平分为两段，然后再递归对分段后的数组再分段
        mergeSegment(arr, minIndex, middleIndex);
        mergeSegment(arr, middleIndex+1, maxIndex);

        // 优化2
        // 只在左数组段最后一个元素比右数组段第一个元素大的时候才执行合并
        if (arr[middleIndex] > arr[middleIndex+1]){
            // 两段数组合并、排序
            segmentsMergeSort(arr, minIndex, middleIndex, maxIndex);
        }
    }

    /**
     * 合并、排序相邻的两个数组段
     * @param arr
     * @param minIndex 左边数组段最小下标
     * @param middleIndex 中间下标
     * @param maxIndex 右边数组段最大下标
     */
    private void segmentsMergeSort(int[] arr, int minIndex, int middleIndex, int maxIndex){

        // 临时数组，存储两个数组段的元素
        int[] segmentArr = new int[maxIndex - minIndex + 1];

        //将数组arr[minIndex]到arr[maxIndex]的元素拷贝到临时数组中
        for (int i = minIndex; i <= maxIndex; i++){
            segmentArr[i-minIndex] = arr[i];
        }

        // 定义分段数组中的下标
        int leftSegmentIndex = minIndex, rightSegmentIndex = middleIndex+1;

        // 比较两数组段元素大小，将临时数组segmentArr中的元素添加到原数组中
        for (int currentIndex=minIndex; currentIndex<=maxIndex; currentIndex++){

            if (leftSegmentIndex > middleIndex){
                // 左边的数组段遍历完了，右边数组段元素加到原数组中
                arr[currentIndex] = segmentArr[rightSegmentIndex - minIndex];
                rightSegmentIndex++;
            }else if (rightSegmentIndex > maxIndex){
                // 右边的数组段遍历完了，左边数组段元素加到原数组中
                arr[currentIndex] = segmentArr[leftSegmentIndex - minIndex];
                leftSegmentIndex++;
            }else if (segmentArr[leftSegmentIndex - minIndex] < segmentArr[rightSegmentIndex - minIndex]){
                // 左边数组段元素小
                arr[currentIndex] = segmentArr[leftSegmentIndex - minIndex];
                leftSegmentIndex++;
            }else {
                // 右边数组段元素小
                arr[currentIndex] = segmentArr[rightSegmentIndex - minIndex];
                rightSegmentIndex++;
            }
        }
    }


    @Test
    public void test_mergeSort() {
        int[] arr1 = generateIntArray(100000, 0, 100000);

        mergeSort(arr1);

        for (int i=0; i<arr1.length; i++){
            System.out.println(arr1[i]);
        }
    }












    //// 快速排序
    //public void quickSort(int[] arr){
    //    quickSort(arr, 0, arr.length-1);
    //}
    //
    //// 快速排序私有方法
    //private void quickSort(int[] arr, int l, int r){
    //    if (l >= r){
    //        return;
    //    }
    //
    //    // 分割、排序数组arr
    //    int p = partition(arr, l, r);
    //    // 左右两边元素递归，使用相同的方式排序
    //    quickSort(arr, l, p-1);
    //    quickSort(arr, p+1, r);
    //}
    //
    //// 分割、排序数组arr，并发返回分割点下标
    //private int partition(int[] arr, int l, int r){
    //    int j = l;
    //
    //    for (int i = l+1; i<=r; i++){
    //        // arr[i]小于arr[l]，j++，之后arr[j]、arr[i]再交换位置
    //        if (arr[i] < arr[l]){
    //            j++;
    //            int v = arr[j];
    //            arr[j] = arr[i];
    //            arr[i] = v;
    //        }
    //    }
    //
    //    // 遍历完后，arr[l]、arr[j] 交换位置
    //    int v = arr[l];
    //    arr[l] = arr[j];
    //    arr[j] = v;
    //
    //    return j;
    //}








    //// 快速排序
    //public void quickSort(int[] arr){
    //    quickSort(arr, 0, arr.length-1);
    //}
    //
    //// 快速排序私有方法
    //private void quickSort(int[] arr, int l, int r){
    //
    //    if (l >= r){
    //        return;
    //    }
    //
    //    // 优化1
    //    // 当需要排序的数据量很小的时候，使用插入排序提高效率
    //    // 这种优化适用于很多的高级算法
    //    // 插入排序请看这篇博客 https://mp.csdn.net/postedit/102546480
    //    if (r - l <= 15){
    //        for (int i = l; i<=r; i++){
    //            int e = arr[i];
    //            int j;
    //            for (j=i; j-1>=l && arr[j-1] > e; j--){
    //                arr[j] = arr[j-1];
    //            }
    //            arr[j] = e;
    //        }
    //        return;
    //    }
    //
    //    // 分割、排序数组arr
    //    int p = partition(arr, l, r);
    //    // 左右两边元素递归，使用相同的方式排序
    //    quickSort(arr, l, p-1);
    //    quickSort(arr, p+1, r);
    //}
    //
    //// 分割、排序数组arr，并发返回分割点下标
    //private int partition(int[] arr, int l, int r){
    //
    //    // 优化2
    //    // 随机选择一个元素和标定点互换位置
    //    int random = l + new Random().nextInt(r - l + 1);
    //    int temp = arr[l];
    //    arr[l] = arr[random];
    //    arr[random] = temp;
    //
    //    // 优化3
    //    // 双路快速排序
    //    int i=l+1, j = r;
    //    while (true){
    //        // arr[i]小于arr[l]，i++向后遍历
    //        while (i <= r && arr[i] < arr[l]){
    //            i++;
    //        }
    //        //arr[j]大于arr[l], j--向前遍历
    //        while (j >= l+1 && arr[j] > arr[l]){
    //            j--;
    //        }
    //        // 退出循环
    //        if (i>j){
    //            break;
    //        }
    //
    //        // 交换位置，避免重复元素都集中在一端
    //        int temp2 = arr[i];
    //        arr[i] = arr[j];
    //        arr[j] = temp2;
    //
    //        i++;
    //        j--;
    //
    //    }
    //
    //    /**
    //     * 上面的循环是通过 break 退出循环，
    //     * 即前面两个内层while循环退出了，则此时必然是 arr[i] >= arr[l]，arr[j] <= arr[l]
    //     * 所以是arr[l]是跟arr[j]交换位置
    //     */
    //    int v = arr[l];
    //    arr[l] = arr[j];
    //    arr[j] = v;
    //
    //    return j;
    //}





    // 快速排序
    public void quickSort(int[] arr){
        quickSort3Ways(arr, 0, arr.length-1);
    }

    // 3路快速排序
    private void quickSort3Ways(int[] arr, int l, int r){

        // 优化1
        // 当需要排序的数据量很小的时候，使用插入排序提高效率
        // 这种优化适用于很多的高级算法
        // 插入排序请看这篇博客 https://mp.csdn.net/postedit/102546480
        if (r - l <= 15){
            for (int i = l; i<=r; i++){
                int e = arr[i];
                int j;
                for (j=i; j-1>=l && arr[j-1] > e; j--){
                    arr[j] = arr[j-1];
                }
                arr[j] = e;
            }
            return;
        }

        // 优化2
        // 随机选择一个元素和标定点互换位置
        int random = l + new Random().nextInt(r - l + 1);
        int temp = arr[l];
        arr[l] = arr[random];
        arr[random] = temp;

        /**
         * 前提：arr[l+1]至arr[lt] 小于 arr[l]
         * 定义lt=1，则初始化时，小于arr[l]的数组段是空的
         */
        int lt = l;
        /**
         * 前提：arr[gt]值arr[r] 大于 arr[l]
         * 定义gt=r+1，则初始化时，大于arr[l]的数组段是空的
         */
        int gt = r+1;

        int i = l+1;

        while (i < gt){
            if (arr[i] < arr[l]){
                int temp2 = arr[i];
                arr[i] = arr[lt+1];
                arr[lt+1] = temp2;

                lt++;
                i++;
            }else if (arr[i] > arr[l]){
                int temp2 = arr[i];
                arr[i] = arr[gt-1];
                arr[gt-1] = temp2;

                gt--;
            }else {
                i++;
            }
        }

        // 遍历完，最后要交换arr[l]与arr[lt]的位置
        int temp3 = arr[l];
        arr[l] = arr[lt];
        arr[lt] = temp3;

        /**
         * 此时 arr[l]至arr[lt-1] 小于 v
         * arr[lt]至arr[gt-1] 等于 v
         * arr[gt]至arr[r] 大于 v
         */
        quickSort3Ways(arr, l, lt-1);
        quickSort3Ways(arr, gt, r);
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
    public void test_quickSort() {

        // 数组中存在大量重复值的情况
        int[] arr1 = generateIntArray(100000, 0, 100000);

        // 快速排序
        Long t1 = new Date().getTime();
        quickSort(arr1);

        for (int i=0; i<arr1.length; i++){
            System.out.println(arr1[i]);
        }

    }







}
