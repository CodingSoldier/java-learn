package com.datastructure_arithmetic_2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class D_Set_MAP {

    /**
     * 4-1 set的使用 Intersection of Two Arrays
     * https://leetcode-cn.com/problems/intersection-of-two-arrays/
     * 给定两个数组，编写一个函数来计算它们的交集。
     */
    static class Solution {
        public int[] intersection(int[] nums1, int[] nums2) {
            HashSet<Integer> set2 = new HashSet<>();
            for (int i = 0; i < nums2.length; i++) {
                set2.add(nums2[i]);
            }

            HashSet<Integer> rSet = new HashSet<>();
            for (int i = 0; i < nums1.length; i++) {
                if (set2.contains(nums1[i])){
                    rSet.add(nums1[i]);
                }
            }

            int[] r = new int[rSet.size()];
            int index = 0;
            for (Integer e : rSet) {
                r[index++] = e;
            }
            return r;
        }
    }


    /**
     * 4-2 map的使用 Intersection of Two Arrays II
     * https://leetcode-cn.com/problems/intersection-of-two-arrays-ii/
     * 给定两个数组，编写一个函数来计算它们的交集。
     *
     * 说明：
     *      输出结果中每个元素出现的次数，应与元素在两个数组中出现次数的最小值一致。
     *      我们可以不考虑输出结果的顺序。
     */
    static public class Solution350 {

        public int[] intersect(int[] nums1, int[] nums2) {
            HashMap<Integer, Integer> record = new HashMap<>();
            for (int i = 0; i < nums1.length; i++) {
                if (record.containsKey(nums1[i])){
                    record.put(nums1[i], record.get(nums1[i])+1);
                }else {
                    record.put(nums1[i], 1);
                }
            }

            ArrayList<Integer> r = new ArrayList<>();
            for (int i = 0; i < nums2.length; i++) {
                if (record.containsKey(nums2[i]) && record.get(nums2[i]) > 0){
                    r.add(nums2[i]);
                    record.put(nums2[i], record.get(nums2[i])-1);
                }
            }

            int[] arr = new int[r.size()];
            int index = 0;
            for (int i : r) {
                arr[index++] = i;
            }

            return arr;
        }

        private static void printArr(int[] arr){
            for(int e: arr)
                System.out.print(e + " ");
            System.out.println();
        }

        public static void main(String[] args) {

        }
    }


    /**
     * 4-4 使用查找表的经典问题 Two Sum
     * https://leetcode-cn.com/problems/two-sum/
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
     */
    static class Solution1 {
        public int[] twoSum(int[] nums, int target) {
            HashMap<Integer, Integer> map = new HashMap<>();

            // 1、遍历数组
            for (int i = 0; i < nums.length; i++) {

                // 2、target - nums[i] 找到另一个数字的值
                int num2 = target - nums[i];

                // 4、如果map有满足条件的数字，则当前下标、前面数字下标组成数组返回
                if (map.containsKey(num2)){
                    int[] arr = {map.get(num2), i};
                    return arr;
                }

                // 3、map中没有满足条件的数字，则将当前数字和下标放入map中
                map.put(nums[i], i);
            }
            throw new  RuntimeException("找不到元素");
        }
    }


    /**
     * 4-5 灵活选择键值 4Sum II
     * https://leetcode-cn.com/problems/4sum-ii/
     *
     */
    static class Solution454 {
        public int fourSumCount(int[] A, int[] B, int[] C, int[] D) {
            HashMap<Integer, Integer> map = new HashMap<>();
            for (int i = 0; i < A.length; i++) {
                for (int j = 0; j < B.length; j++) {
                    if (map.containsKey(A[i] + B[j])){
                        map.put(A[i]+B[j], map.get(A[i] + B[j])+1);
                    }else {
                        map.put(A[i]+B[j], 1);
                    }
                }
            }

            int r = 0;
            for (int i = 0; i < C.length; i++) {
                for (int j = 0; j < D.length; j++) {
                    if (map.containsKey(0 - C[i] - D[j])){
                        r += map.get(0 - C[i] - D[j]);
                    }
                }
            }

            return r;
        }
    }


    /**
     * 4-6 灵活选择键值 Number of Boomerangs
     * https://leetcode-cn.com/problems/number-of-boomerangs/submissions/
     */
    static class Solution447 {
        public int numberOfBoomerangs(int[][] points) {

            int r = 0;

            for (int i = 0; i < points.length; i++) {
                HashMap<Integer, Integer> map = new HashMap<>();
                for (int j = 0; j < points.length; j++) {
                    if (i != j){
                        int dist = pid(points[i], points[j]);
                        if (map.containsKey(dist)){
                            map.put(dist, map.get(dist)+1);
                        }else{
                            map.put(dist, 1);
                        }
                    }
                }

                for (Integer key : map.keySet()) {
                    if (map.get(key).compareTo(2) >= 0){
                        r += map.get(key) * (map.get(key)-1);
                    }
                }
            }
             return r;
        }

        public static int pid(int[] pa, int[] pb){
            return (pa[0] - pb[0]) * (pa[0] - pb[0])
                    + (pa[1] - pb[1]) * (pa[1] - pb[1]);
        }
    }


    /**
     * 219. 存在重复元素 II
     * https://leetcode-cn.com/problems/contains-duplicate-ii/
     */
    static class Solution219 {
        public boolean containsNearbyDuplicate(int[] nums, int k) {
            HashSet<Integer> set = new HashSet<>();
            for (int i = 0; i < nums.length; i++) {
                if (set.contains(nums[i])){
                    return true;
                }

                set.add(nums[i]);

                if (set.size() == k+1){
                    set.remove(nums[i - k]);
                }
            }
            return false;
        }

    }


    /**
     * 220. Contains Duplicate III
     * https://leetcode-cn.com/problems/contains-duplicate-iii/
     */
    static class Solution220 {
        public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
            TreeSet<Long> ts = new TreeSet<>();
            for (int i = 0; i < nums.length; i++) {
                if (ts.ceiling((long)nums[i] - (long)t) != null
                        && ts.ceiling((long)nums[i] - (long)t) <= (long)nums[i] + (long)t){
                    return true;
                }
                ts.add((long)nums[i]);

                if (ts.size() == k+1){
                    ts.remove((long)nums[i -k]);
                }
            }
            return false;
        }
    }

}
