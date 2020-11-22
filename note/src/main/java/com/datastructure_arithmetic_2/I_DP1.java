package com.datastructure_arithmetic_2;

import java.util.Arrays;

public class I_DP1 {

    static class Fib01{

        static int num=0;

        public static long fib(int n){
            num++;
            if (n == 0){
                return 0;
            }
            if (n == 1){
                return 1;
            }

            return fib(n -1) + fib(n - 2);
        }

        public static void main(String[] args) {
            long fib = fib(20);
            System.out.println(fib);
            System.out.println("计算的次数num="+num);
        }
    }

    /**
     * 记忆化搜索
     * 自上而下地解决问题
     */
    static class Fib02{

        static long num=0;

        /**
         * 存储已经计算过的fib(n)，减少fib的调用次数
         */
        static long[] memo = new long[21];
        static {
            Arrays.fill(memo, -1);
        }

        /**
         * 返回结果使用long，避免整型溢出
         */
        public static long fib(int n){
            num++;
            if (n == 0){
                return 0;
            }
            if (n == 1){
                return 1;
            }

            if (memo[n] == -1){
                /**
                 * 自上而下解决问题
                 */
                long r = fib(n - 1) + fib(n - 2);
                memo[n] = r;
            }

            return memo[n];
        }

        public static void main(String[] args) {
            long fib = fib(20);
            System.out.println(fib);
            System.out.println("计算的次数num="+num);
        }
    }

    /**
     * 动态规划
     * 自下而上地解决问题
     */
    static class Fib03{

        static long num=0;

        public static long fib(int n){
            num++;

            long[] memo = new long[n+1];
            Arrays.fill(memo, -1);
            memo[0] = 0;
            memo[1] = 1;

            /**
             * 动态规划
             * 知道了memo[0]、memo[1]的值，自下而上解决问题
             */
            for (int i = 2; i <= n; i++) {
                /**
                 * 首次执行时，memo[2]=memo[1] + memo[0]
                 */
                memo[i] = memo[i-1] + memo[i-2];
            }

            return memo[n];
        }

        public static void main(String[] args) {
            long fib = fib(20);
            System.out.println(fib);
            System.out.println("计算的次数num="+num);
        }
    }

/*
    动态规划
    将原问题拆解成若干子问题，同时保存子问题的答案，使得每个子问题只求解一次，最终获得原问题的答案
 */

}
