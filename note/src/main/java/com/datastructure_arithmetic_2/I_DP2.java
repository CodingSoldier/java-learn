package com.datastructure_arithmetic_2;

import java.util.Arrays;

public class I_DP2 {

    /**
     * https://leetcode-cn.com/problems/climbing-stairs/
     * 9-2 第一个动态规划问题 Climbing Stairs
     */
    static class Solution70 {

        //public int climbStairs(int n) {
        //    //1阶台阶，有1种方法
        //    if (n == 1){
        //        return 1;
        //    }
        //    //2阶台阶，有2种方法
        //    if (n == 2){
        //        return 2;
        //    }
        //    return climbStairs(n-1) + climbStairs(n-2);
        //}

        /**
         * 动态规划解法
         */
        public int climbStairs(int n) {
            if (n <= 2) {
                return n;
            }
            int[] memo = new int[n + 1];
            memo[1] = 1;
            memo[2] = 2;
            for (int i = 3; i <= n; i++) {
                memo[i] = memo[i - 1] + memo[i - 2];

            }
            return memo[n];
        }
    }


    /**
     * 9-3 发现重叠子问题 Integer Break
     * https://leetcode-cn.com/problems/integer-break/
     */
    static class Solution343 {
        int[] memo = null;

        int max3(int l1, int l2, int l3) {
            return Math.max(l1, Math.max(l2, l3));
        }

        /**
         * 记忆化搜索解法
         */
        //public int integerBreak(int n) {
        //    memo = new int[n+1];
        //    Arrays.fill(memo, -1);
        //
        //    return breakInteger(n);
        //}
        //
        //// 分隔n，至少将n分割为两部分
        //public int breakInteger(int n){
        //    if (n == 1){
        //        return 1;
        //    }
        //    if (memo[n] != -1){
        //        return memo[n];
        //    }
        //    int res = memo[n];
        //    for (int i = 1; i <= n; i++) {
        //        /**
        //         * breakInteger(int n)的定义是将n至少分割为两部分
        //         * i * breakInteger(n - i) 至少是3个数的乘积
        //         * 所以要加上 i*(n-i)
        //         */
        //        res = max3(res, i * breakInteger(n - i), i * (n-i));
        //    }
        //
        //    memo[n] = res;
        //    return res;
        //}


        /**
         * 动态规划解法
         */
        public int integerBreak(int n) {
            if (n <= 1) {
                return n;
            }
            int[] memo = new int[n + 1];
            memo[1] = 1;
            for (int i = 2; i <= n; i++) {
                for (int j = 1; j <= i - 1; j++) {
                    memo[i] = max3(memo[i], j * (i - j), j * memo[i - j]);
                }
            }
            return memo[n];
        }
    }


    /**
     * 9-8 LIS问题 Longest Increasing Subsequence
     * https://leetcode-cn.com/problems/longest-increasing-subsequence/
     */
    static class Solution {
        public int lengthOfLIS(int[] nums) {
            if (nums == null){
                return 0;
            }

            int[] memo = new int[nums.length];
            Arrays.fill(memo, 1);

            for (int i = 1; i < nums.length; i++) {
                for (int j = 0; j < i; j++) {
                    if (nums[i] > nums[j]){
                        memo[i] = Math.max(memo[i], memo[j]+1);
                    }
                }
            }

            int r = 1;
            for (int i = 0; i < memo.length; i++) {
                r = Math.max(r, memo[i]);
            }

            return r;
        }
    }


}
