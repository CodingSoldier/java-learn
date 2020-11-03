package com.datastructure_arithmetic_2;

import org.junit.Test;

import java.util.ArrayList;

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

    /**
     * 如何写出正确的算法
     *      1、明确变量含义
     *      2、定义循环不变量，并维护循环不变量含义一致
     *      3、小数据量调试
     */


    /**
     * https://leetcode-cn.com/problems/move-zeroes/submissions/
     * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
     * 输入: [0,1,0,3,12]
     * 输出: [1,3,12,0,0]
     *
     */
    static class Solution {

        /**
         * 1、所有非零元素临时存储
         * 2、非零元素添加到原数组
         * 3、后面的元素设置为0
         *
         * 此算法的时间复杂度O(n)、空间复杂度O(n)
         */
        public void moveZeroes1(int[] nums) {
            ArrayList<Integer> tempList = new ArrayList<>();
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != 0){
                    tempList.add(nums[i]);
                }
            }
            for (int i = 0; i < tempList.size(); i++) {
                nums[i]=tempList.get(i);
            }
            for (int i = tempList.size(); i < nums.length; i++) {
                nums[i] = 0;
            }
        }

        /**
         * 图 C01-moveZeroes2.jpg
         * 时间复杂度O(n)
         * 空间复杂度O(1)
         * @param nums
         */
        public void moveZeroes2(int[] nums){
            int k = 0;
            // 设置一个游标，[0, k)存储非零元素，[k, nums.length]为0
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] != 0){
                    nums[k++] = nums[i];
                }
            }
            for (int i = k; i < nums.length; i++) {
                nums[i] = 0;
            }
        }

        /**
         * C01-moveZeroes3.jpg
         * 非零元素与零交换位置，发生交换就k++
         */
        public static void moveZeroes3(int[] nums){
            int k = 0;
            for (int i = 0; i < nums.length; i++) {
                // 找到非零元素
                if (nums[i] != 0){
                    /**
                     * 整个数组都是非零元素
                     * 此时 k == i ，为了避免自己跟自己发生交换，可加上判断，只有k != i的时候才交换
                     * k == i 时，k++，i在循环中也会++
                     */
                    if (k != i){
                        swap(nums, k++, i);
                    } else {
                        k++;
                    }
                }
            }
        }

        public static void swap(int[] nums, int i, int j){
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }

        public static void main(String[] args) {
            Solution solution = new Solution();
            int[] nums = {0,1,0,3,12};

            //solution.moveZeroes1(nums);

            solution.moveZeroes2(nums);

            for (int i = 0; i < nums.length; i++) {
                System.out.println(nums[i]);
            }

        }
    }




    /**
     * https://leetcode-cn.com/problems/sort-colors/submissions/
     * 给定一个包含红色、白色和蓝色，一共 n 个元素的数组，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
     *
     * 此题中，我们使用整数 0、 1 和 2 分别表示红色、白色和蓝色。
     *
     */
    static class Solution75 {

        /**
         * 计数排序。
         * 计算0、1、2元素各自的数量，将等数量个数字添加数组中
         */
        public void sortColors(int[] nums) {
            int[] counts = {0, 0, 0};
            for (int i = 0; i < nums.length; i++) {
                counts[nums[i]]++;
            }
            int index = 0;
            for (int i = 0; i < counts[0]; i++) {
                nums[index++] = 0;
            }
            for (int i = 0; i < counts[1]; i++) {
                nums[index++] = 1;
            }
            for (int i = 0; i < counts[2]; i++) {
                nums[index++] = 2;
            }
        }

        /**
         * 模仿3路快排解决
         * @param nums
         */
        public void sortColor2(int[] nums){
            /**
             * 选取标定点，明确循环不变量
             * [zeroStart, ....]的元素都是0
             * [twoStart, nums.length-1]的元素都是2
             */
            int zeroStart = -1;
            int twoStart = nums.length;
            /**
             * i < twoStart 能保证[twoStart, nums.length-1]有效
             * for循环最后没有i++，在循环体内i++
             */
            for (int i = 0; i < twoStart; ) {
                if (nums[i] == 1){
                    i++;
                }else if (nums[i] == 2){
                    /**
                     * 如果元素是2，则交换--twoStart和当前元素
                     * 在循环体中保持循环不变量有效
                     * --twoStart依旧保证[twoStart, nums.length-1]有效
                     */
                    swap(nums, --twoStart, i);
                }else {
                    /**
                     * 如果元素是0，交换++zeroStart和当前元素
                     * [zeroStart, ....] 依旧有效
                     */
                    swap(nums, ++zeroStart, i++);
                }
            }
        }

        private void swap(int[] nums, int i, int j){
            int t = nums[i];
            nums[i]= nums[j];
            nums[j] = t;
        }

    }



    /**
     * https://leetcode-cn.com/problems/two-sum-ii-input-array-is-sorted/
     * 给定一个已按照升序排列 的有序数组，找到两个数使得它们相加之和等于目标数。
     * 函数应该返回这两个下标值 index1 和 index2，其中 index1 必须小于 index2。
     *
     * 说明:
     *      返回的下标值（index1 和 index2）不是从零开始的。
     *      你可以假设每个输入只对应唯一的答案，而且你不可以重复使用相同的元素。
     *
     * 输入: numbers = [2, 7, 11, 15], target = 9
     * 输出: [1, 2]
     * 解释: 2 与 7 之和等于目标数 9。因此 index1 = 1, index2 = 2 。
     *
     */
    class Solution167 {
        //public int[] twoSum(int[] numbers, int target) {
        //
        //}
    }






    static class KuaiPai{
        public void quickSort(int[] arr){
            quickSort(arr, 0, arr.length);
        }

        public void quickSort(int[] arr, int l, int r){
            if (l >= r){
                return;
            }

            int p = partition(arr, l, r);
            quickSort(arr, l, p-1);
            quickSort(arr, p+1, r);
        }

        public int partition(int[] arr, int l, int r){
            int j = l;
            for (int i = l; i <= r; i++) {
                if (arr[i] < arr[l]){
                    int temp = arr[i];
                    arr[]
                }

            }

        }
    }










}
