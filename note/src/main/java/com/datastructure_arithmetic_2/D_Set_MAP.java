package com.datastructure_arithmetic_2;

import java.util.HashSet;

public class D_Set_MAP {

    /**
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
}
