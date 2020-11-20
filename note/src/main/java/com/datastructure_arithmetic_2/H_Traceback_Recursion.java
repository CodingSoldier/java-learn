package com.datastructure_arithmetic_2;

import java.util.LinkedList;
import java.util.List;

public class H_Traceback_Recursion {

    /**
     * 8-1 树形问题 Letter Combinations of a Phone Number
     * https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/
     */
    static class Solution17 {
        private String letterMap[] = {
                " ",    //0
                "",     //1
                "abc",  //2
                "def",  //3
                "ghi",  //4
                "jkl",  //5
                "mno",  //6
                "pqrs", //7
                "tuv",  //8
                "wxyz"  //9
        };

        private LinkedList<String> res = new LinkedList<>();

        public List<String> letterCombinations(String digits) {
            if ("".equals(digits)){
                return res;
            }
            findCombinations(digits, 0, "");
            return res;
        }

        private void findCombinations(String digits, int digitsIndex, String resStr){
            if (digits.length() == digitsIndex){
                res.add(resStr);
                return ;
            }

            char c = digits.charAt(digitsIndex);
            String letterStrs = letterMap[c - '0'];
            for (int i = 0; i < letterStrs.length(); i++) {
                findCombinations(digits, digitsIndex+1, resStr+letterStrs.charAt(i));
            }
        }

        public static void main(String[] args) {
            List<String> list = new Solution17().letterCombinations("23");
            System.out.println(list.toString());
        }
    }


    /**
     * 8-3 排列问题 Permutations
     * https://leetcode-cn.com/problems/permutations/
     * 回溯法求全排列
     */

    static class Solution46 {

        private List<List<Integer>> res = new LinkedList<>();

        public List<List<Integer>> permute(int[] nums) {
            if (nums == null){
                return new LinkedList<>();
            }

            pushPermute(nums, 0, new LinkedList());

            return res;
        }

        public void pushPermute(int[] nums, int index, LinkedList<Integer> elemList){
            System.out.println(String.format("index=%s，elemList=%s", index, elemList));
            if (nums.length == elemList.size()){
                res.add((LinkedList<Integer>)elemList.clone());
                return;
            }

            for (int i = 0; i < nums.length; i++) {
                if (!elemList.contains(nums[i])){
                    elemList.addLast(nums[i]);
                    System.out.println(String.format("index=%s，i=%s，elemList=%s", index, i, elemList));
                    pushPermute(nums, index + 1, elemList);
                    elemList.removeLast();
                }
            }
        }

        public static void main(String[] args) {
            int[] arr = {1, 2, 3};
            List<List<Integer>> l = new Solution46().permute(arr);
            System.out.println(l);
        }

    }
}
