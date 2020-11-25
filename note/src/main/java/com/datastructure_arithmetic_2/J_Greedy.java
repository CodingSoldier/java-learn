package com.datastructure_arithmetic_2;

import java.util.ArrayList;
import java.util.Arrays;

public class J_Greedy {

    /**
     * 10-1 贪心基础 Assign Cookies
     * https://leetcode-cn.com/problems/assign-cookies/
     */
    static class Solution455 {
        public int findContentChildren(int[] g, int[] s) {
            ArrayList<Integer> greedyList = new ArrayList<>();
            for (int i = 0; i < g.length; i++) {
                greedyList.add(g[i]);
            }
            ArrayList<Integer> sizeList = new ArrayList<>();
            for (int i = 0; i < s.length; i++) {
                sizeList.add(s[i]);
            }

            greedyList.sort((Integer i1, Integer i2) -> i2 - i1);
            sizeList.sort((Integer i1, Integer i2) -> i2 - i1);

            int r = 0;
            int i = 0;
            int j = 0;
            while (i<greedyList.size() && j<sizeList.size()){
                if (sizeList.get(j) >= greedyList.get(i)){
                    r++;
                    j++;
                    i++;
                }else {
                    i++;
                }
            }
            return r;
        }
    }


    /**
     * https://leetcode-cn.com/problems/non-overlapping-intervals/description/
     * 10-2 贪心算法与动态规划的关系 Non-overlapping Intervals
     */
    static class Solution435 {
        public static class Interval{
            int start;
            int end;
            Interval(){
                start = 0;
                end = 0;
            }
            Interval(int s, int e){
                start = 0;
                end = 0;
            }
        }

        public int eraseOverlapIntervals(Interval[] intervals) {
            if (intervals.length == 0){
                return 0;
            }
            Arrays.sort(intervals, (Interval o1, Interval o2) -> {
                if (o1.start != o2.start){
                    return o1.start - o2.start;
                }
                return o1.end - o2.end;
            });

            //// 动态规划解法
            //int[] memo = new int[intervals.length];
            //Arrays.fill(memo, 1);
            //for (int i = 1; i < intervals.length; i++) {
            //    for (int j = 0; j < i; j++) {
            //        if (intervals[i].start >= intervals[j].end){
            //            memo[i] = Math.max(memo[i], memo[j]+1);
            //        }
            //    }
            //}
            //
            //int res = 1;
            //for (int i = 0; i < memo.length; i++) {
            //    res = Math.max(res, memo[i]);
            //}


            // 贪心算法解法
            int res = 1;
            int pre = 0;
            for (int i = 1; i < intervals.length; i++) {
                if (intervals[i].start >= intervals[pre].end){
                    res++;
                    pre = i;
                }
            }

            return intervals.length - res;
        }
    }
}
