package com.datastructure_arithmetic_2;

import java.util.ArrayList;

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
}
