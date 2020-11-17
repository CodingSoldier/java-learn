package com.datastructure_arithmetic_2;

import java.util.Stack;

public class F_Stack {

    /**
     *  6-1 栈的基础应用 Valid Parentheses
     * https://leetcode-cn.com/problems/valid-parentheses/
     */
    static class Solution {
        public boolean isValid(String s) {
            Stack<Character> stack = new Stack<>();
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(' || s.charAt(i) == '[' || s.charAt(i) == '{'){
                    stack.push(s.charAt(i));
                }else {
                    if (stack.size() == 0){
                        return false;
                    }

                    Character match = null;
                    if (s.charAt(i) == ')'){
                        match = '(';
                    }else if (s.charAt(i) == ']'){
                        match = '[';
                    }else if (s.charAt(i) == '}'){
                        match = '{';
                    }else {
                        return false;
                    }

                    Character top = stack.pop();
                    if (top != match){
                        return false;
                    }
                }
            }

            if (stack.size() != 0){
                return false;
            }

            return true;
        }
    }

}
